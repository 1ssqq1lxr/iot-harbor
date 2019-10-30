package com.iot.transport.server.handler.pub;

import com.iot.api.MqttMessageApi;
import com.iot.api.RsocketConfiguration;
import com.iot.api.RsocketTopicManager;
import com.iot.api.TransportConnection;
import com.iot.common.message.TransportMessage;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.DirectHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
public class PubHandler implements DirectHandler {


    @Override
    public void handler(MqttMessage message, TransportConnection connection, RsocketConfiguration config) {
            RsocketServerConfig serverConfig = (RsocketServerConfig) config;
            MqttFixedHeader header = message.fixedHeader();
            switch (header.messageType()) {
                case PUBLISH:
                    MqttPublishMessage mqttPublishMessage = (MqttPublishMessage) message;
                    MqttPublishVariableHeader variableHeader = mqttPublishMessage.variableHeader();
                    ByteBuf byteBuf = mqttPublishMessage.payload();
                    byte[] bytes = copyByteBuf(byteBuf);
                    if (header.isRetain()) {//保留消息
                        serverConfig.getMessageHandler().saveRetain(header.isDup(), header.isRetain(), header.qosLevel().value(), variableHeader.topicName(), bytes);
                    }
                    switch (header.qosLevel()) {
                        case AT_MOST_ONCE:
                            serverConfig.getTopicManager().getConnectionsByTopic(variableHeader.topicName())
                                    .stream()// 过滤掉本身 已经关闭的dispose
                                    .forEach(c -> c.sendMessage(false, header.qosLevel(), header.isRetain(), variableHeader.topicName(),bytes).subscribe());
                            break;
                        case AT_LEAST_ONCE:
                            MqttPubAckMessage mqttPubAckMessage = MqttMessageApi.buildPuback(header.isDup(), header.qosLevel(), header.isRetain(), variableHeader.packetId()); // back
                            connection.write(mqttPubAckMessage).subscribe();
                            serverConfig.getTopicManager().getConnectionsByTopic(variableHeader.topicName())
                                    .stream().filter(c -> connection.equals(c) || c.isDispose())
                                    .forEach(c -> c.sendMessageRetry(false, header.qosLevel(), header.isRetain(), variableHeader.topicName(),bytes).subscribe());
                            break;
                        case EXACTLY_ONCE:
                            int messageId = variableHeader.packetId();
                            MqttPubAckMessage mqttPubRecMessage = MqttMessageApi.buildPubRec(messageId);
                            connection.write(mqttPubRecMessage).subscribe();  //  send rec
                            connection.addDisposable(messageId, Mono.fromRunnable(() ->
                                    connection.write(MqttMessageApi.buildPubRel(messageId)).subscribe())
                                    .delaySubscription(Duration.ofSeconds(10)).repeat().subscribe()); // retry
                            TransportMessage transportMessage = TransportMessage.builder().isRetain(header.isRetain())
                                    .isDup(false)
                                    .topic(variableHeader.topicName())
                                    .message(bytes)
                                    .qos(header.qosLevel().value())
                                    .build();
                            connection.saveQos2Message(messageId, transportMessage);
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
                    int id = recVH.messageId();
                    connection.cancleDisposable(id);
                    connection.write(MqttMessageApi.buildPubRel(id)).subscribe();  //  send rel
                    connection.addDisposable(id, Mono.fromRunnable(() ->
                            connection.write(MqttMessageApi.buildPubRel(id)).subscribe())
                            .delaySubscription(Duration.ofSeconds(10)).repeat().subscribe()); // retry
                    break;
                case PUBREL:
                    MqttPubAckMessage rel = (MqttPubAckMessage) message;
                    int messageId = rel.variableHeader().messageId();
                    connection.cancleDisposable(messageId); // cacel replay rec
                    MqttPubAckMessage mqttPubRecMessage = MqttMessageApi.buildPubComp(messageId);
                    connection.write(mqttPubRecMessage).subscribe();  //  send comp
                    connection.getAndRemoveQos2Message(messageId)
                            .ifPresent(msg -> serverConfig.getTopicManager().getConnectionsByTopic(msg.getTopic())
                                    .stream().filter(c -> connection.equals(c) || c.isDispose())
                                    .forEach(c -> c.sendMessageRetry(false, MqttQoS.valueOf(msg.getQos()), header.isRetain(), msg.getTopic(),msg.getMessage()).subscribe()));
                    break;
                case PUBCOMP:
                    MqttMessageIdVariableHeader compVH = (MqttMessageIdVariableHeader) message.variableHeader();
                    connection.cancleDisposable(compVH.messageId());
                    break;
            }
    }



    private byte[] copyByteBuf(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
