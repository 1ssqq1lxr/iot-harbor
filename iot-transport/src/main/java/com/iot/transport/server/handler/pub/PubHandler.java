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
import reactor.netty.Connection;

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
                                    .stream().filter(connection::equals).forEach(c ->
                                c.write( MqttMessageApi.buildPub(false, header.qosLevel(), header.isRetain(), variableHeader.messageId(), variableHeader.topicName(), Unpooled.wrappedBuffer(bytes))).subscribe()
                            );
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
                        case FAILURE:
                            log.error(" publish FAILURE {} {} ", header, variableHeader);
                            break;
                    }
                    break;
                case PUBACK:
                    MqttPubAckMessage mqttPubAckMessage = (MqttPubAckMessage) message;
                    connection.cancleDisposable(mqttPubAckMessage.variableHeader().messageId());
                    break;
                case PUBREC:
                    MqttPubAckMessage recMessage = (MqttPubAckMessage) message;
                    int id=recMessage.variableHeader().messageId();
                    connection.write( MqttMessageApi.buildPubRel(id)).subscribe();  //  send rel
                    break;
                case PUBREL:
                    MqttPubAckMessage ackMessage = (MqttPubAckMessage) message;
                    MqttPubAckMessage mqttPubRecMessage = MqttMessageApi.buildPubComp(ackMessage.variableHeader().messageId());
                    connection.write(mqttPubRecMessage).subscribe();  //  send rec
                    if(!header.isDup()) { // 不是重发
                        MqttPubAckMessage  rel = (MqttPubAckMessage)message;
                        int messageId= rel.variableHeader().messageId();
                        TransportMessage transportMessage =connection.getAndRemoveQos2Message(messageId);
                        sendPub(config.getTopicManager(),transportMessage.getTopic(),connection,MqttQoS.valueOf(transportMessage.getQos()),transportMessage.isRetain(),Unpooled.wrappedBuffer(transportMessage.getMessage()));
                    }
                    break;
                case PUBCOMP:
                    MqttPubAckMessage  compMessage = (MqttPubAckMessage) message;
                    connection.cancleDisposable(compMessage.variableHeader().messageId());
                    break;
            }
        });
    }

    private void sendPub(RsocketTopicManager topicManager, String topic, TransportConnection connection,MqttQoS qos,boolean retain,ByteBuf byteBuf){
        topicManager.getConnectionsByTopic(topic)
                .stream().filter(connection::equals).forEach(c -> {
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
