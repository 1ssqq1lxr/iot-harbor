package com.iot.transport.server.handler.pub;

import com.iot.api.MqttMessageApi;
import com.iot.common.connection.TransportConnection;
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
                                    .stream().filter(connection::equals).forEach(c ->
                                c.write( MqttMessageApi.buildPub(false, header.qosLevel(), header.isRetain(), variableHeader.messageId(), variableHeader.topicName(), Unpooled.wrappedBuffer(bytes))).subscribe()
                            );
                            break;
                        case AT_LEAST_ONCE:
                            MqttPubAckMessage mqttPubAckMessage = MqttMessageApi.buildPuback(header.isDup(), header.qosLevel(), header.isRetain(), variableHeader.packetId()); // back
                            connection.write(mqttPubAckMessage).subscribe();
                            config.getTopicManager().getConnectionsByTopic(variableHeader.topicName())
                                    .stream().filter(connection::equals).forEach(c -> {
                                int id = c.messageId();
                                c.addDisposable(id, Mono.fromRunnable(() ->
                                        connection.write( MqttMessageApi.buildPub(true, header.qosLevel(), header.isRetain(), id, variableHeader.topicName(), Unpooled.wrappedBuffer(bytes))).subscribe())
                                        .delaySubscription(Duration.ofSeconds(10)).repeat().subscribe()); // retry
                                MqttPublishMessage publishMessage = MqttMessageApi.buildPub(false, header.qosLevel(), header.isRetain(), id, variableHeader.topicName(), Unpooled.wrappedBuffer(bytes)); // pub
                                c.write(publishMessage).subscribe();
                            });
                            break;
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
                    break;
                case PUBREL:
                    break;
                case PUBCOMP:
                    break;
            }
        });
    }

    private byte[] copyByteBuf(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
