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

@Slf4j
public class PubHandler implements DirectHandler {


    @Override
    public Mono<Void> handler(MqttMessage message, TransportConnection connection, RsocketServerConfig config) {
        return Mono.fromRunnable(()->{
            MqttPublishMessage mqttPublishMessage =(MqttPublishMessage) message;
            MqttFixedHeader header=mqttPublishMessage.fixedHeader();
            MqttPublishVariableHeader variableHeader=mqttPublishMessage.variableHeader();
            ByteBuf byteBuf=mqttPublishMessage.payload();
            switch (mqttPublishMessage.fixedHeader().messageType()){
                case PUBLISH:
                        if(header.isRetain()){//保留消息
                            config.getMessageHandler().saveRetain(header.isDup(),header.isRetain(),header.qosLevel().value(),variableHeader.topicName(),copyByteBuf(byteBuf));
                        }
                        config.getTopicManager().getConnectionsByTopic(variableHeader.topicName())
                                .stream().forEach(c->{
                            switch (header.qosLevel()){
                                case AT_MOST_ONCE:
                                    MqttPublishMessage publishMessage = MqttMessageApi.buildPub(false,header.qosLevel(),header.isRetain(),c.messageId(),variableHeader.topicName(),Unpooled.wrappedBuffer(byteBuf));
                                    c.write(publishMessage).subscribe();
                                    break;
                                case AT_LEAST_ONCE:
                                    MqttPubAckMessage mqttPubAckMessage= MqttMessageApi.buildPuback(header.isDup(),header.qosLevel(),header.isRetain(),variableHeader.messageId()); // back
                                    c.write(mqttPubAckMessage).subscribe();
                                    break;
                                case FAILURE:
                                    log.error(" publish FAILURE {} {} ",header,variableHeader);
                                    break;
                            }
                        });
                    break;
                case PUBACK:
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

    private    byte[]  copyByteBuf(ByteBuf byteBuf){
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
