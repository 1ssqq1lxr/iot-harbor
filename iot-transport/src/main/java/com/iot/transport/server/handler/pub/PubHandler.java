package com.iot.transport.server.handler.pub;

import com.iot.api.MqttMessageApi;
import com.iot.api.RsocketTopicManager;
import com.iot.common.connection.TransportConnection;
import com.iot.common.message.TransportMessage;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.server.handler.DirectHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
public class PubHandler implements DirectHandler {


    @Override
    public Mono<Void> handler(MqttMessage message, TransportConnection connection, RsocketServerConfig config) {
        return Mono.fromRunnable(() -> {
            MqttFixedHeader header = message.fixedHeader();
            switch (header.messageType()) {
                case PUBLISH:
                    MqttPublishMessage mqttPublishMessage = (MqttPublishMessage) message;
                    MqttPublishVariableHeader variableHeader = mqttPublishMessage.variableHeader();
                    ByteBuf byteBuf = mqttPublishMessage.payload();
                    byte[] bytes=copyByteBuf(byteBuf);
                    if (header.isRetain()) {//保留消息
                        config.getMessageHandler().saveRetain(header.isDup(), header.isRetain(), header.qosLevel().value(), variableHeader.topicName(), bytes);
                    }
                    switch (header.qosLevel()) {
                        case AT_MOST_ONCE:
                            config.getTopicManager().getConnectionsByTopic(variableHeader.topicName())
                                    .stream().filter(c->connection.equals(c)|| c.isDispose() ) // 过滤掉本身 已经关闭的dispose
                                    .forEach(c ->
                                     c.write( MqttMessageApi.buildPub(false, header.qosLevel(), header.isRetain(), variableHeader.messageId(), variableHeader.topicName(), Unpooled.wrappedBuffer(bytes))).subscribe());
                            break;
                        case AT_LEAST_ONCE:
                            MqttPubAckMessage mqttPubAckMessage = MqttMessageApi.buildPuback(header.isDup(), header.qosLevel(), header.isRetain(), variableHeader.packetId()); // back
                            connection.write(mqttPubAckMessage).subscribe();
                            if(!header.isDup()){ // 不是重发
                                sendPub(config.getTopicManager(),variableHeader.topicName(),connection,header.qosLevel(),header.isRetain(),Unpooled.wrappedBuffer(bytes));
                            }
                            break;
                        case EXACTLY_ONCE:
                            int messageId=variableHeader.packetId();
                            MqttPubAckMessage mqttPubRecMessage = MqttMessageApi.buildPubRec(messageId);
                            connection.write(mqttPubRecMessage).subscribe();  //  send rec
                            TransportMessage transportMessage=TransportMessage.builder().isRetain(header.isRetain())
                                    .isDup(false)
                                    .topic(variableHeader.topicName())
                                    .message(bytes)
                                    .qos(header.qosLevel().value())
                                    .build();
                            connection.saveQos2Message(messageId,transportMessage);
                            break;
                        case FAILURE:
                            log.error(" publish FAILURE {} {} ", header, variableHeader);
                            break;
                    }
                    break;
                case PUBACK:
                    MqttMessageIdVariableHeader Back = (MqttMessageIdVariableHeader) message.variableHeader();
                    connection.cancleDisposable(Back.messageId());
                    break;
                case PUBREC:
                    MqttMessageIdVariableHeader recVH = (MqttMessageIdVariableHeader) message.variableHeader();
                    int id=recVH.messageId();
                    connection.write( MqttMessageApi.buildPubRel(id)).subscribe();  //  send rel
                    break;
                case PUBREL:
                    MqttMessageIdVariableHeader RelVH = (MqttMessageIdVariableHeader) message.variableHeader();
                    MqttPubAckMessage mqttPubRecMessage = MqttMessageApi.buildPubComp(RelVH.messageId());
                    connection.write(mqttPubRecMessage).subscribe();  //  send rec
                    if(!header.isDup()) { // 不是重发
                        MqttPubAckMessage  rel = (MqttPubAckMessage)message;
                        int messageId= rel.variableHeader().messageId();
                        TransportMessage transportMessage =connection.getAndRemoveQos2Message(messageId);
                        sendPub(config.getTopicManager(),transportMessage.getTopic(),connection,MqttQoS.valueOf(transportMessage.getQos()),transportMessage.isRetain(),Unpooled.wrappedBuffer(transportMessage.getMessage()));
                    }
                    break;
                case PUBCOMP:
                    MqttMessageIdVariableHeader compVH = (MqttMessageIdVariableHeader) message.variableHeader();
                    connection.cancleDisposable(compVH.messageId());
                    break;
            }
        });
    }

    private void sendPub(RsocketTopicManager topicManager, String topic, TransportConnection connection,MqttQoS qos,boolean retain,ByteBuf byteBuf){
        topicManager.getConnectionsByTopic(topic)
                .stream().filter(c->connection.equals(c)|| c.isDispose() )
                .forEach(c -> {
                    int id = c.messageId();
                    c.addDisposable(id, Mono.fromRunnable(() ->
                            connection.write( MqttMessageApi.buildPub(true, qos, retain, id, topic, byteBuf)).subscribe())
                            .delaySubscription(Duration.ofSeconds(10)).repeat().subscribe()); // retry
                    MqttPublishMessage publishMessage = MqttMessageApi.buildPub(false, qos,retain, id, topic, byteBuf); // pub
                    c.write(publishMessage).subscribe();
                });
    }

    private byte[] copyByteBuf(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
