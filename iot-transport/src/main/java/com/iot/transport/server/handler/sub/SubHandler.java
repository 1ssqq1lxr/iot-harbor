package com.iot.transport.server.handler.sub;

import com.iot.api.MqttMessageApi;
import com.iot.api.RsocketConfiguration;
import com.iot.api.RsocketTopicManager;
import com.iot.api.TransportConnection;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.DirectHandler;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class SubHandler implements DirectHandler {


    @Override
    public void handler(MqttMessage message, TransportConnection connection, RsocketConfiguration config) {
            RsocketServerConfig serverConfig = (RsocketServerConfig) config;
            MqttFixedHeader header = message.fixedHeader();
            switch (header.messageType()) {
                case SUBSCRIBE:
                    MqttSubscribeMessage subscribeMessage = (MqttSubscribeMessage) message;
                    int messageId = subscribeMessage.variableHeader().messageId();
                    List<Integer> grantedQoSLevels = subscribeMessage.payload().topicSubscriptions().stream().map(m -> m.qualityOfService().value()).collect(Collectors.toList());
                    MqttSubAckMessage mqttSubAckMessage = MqttMessageApi.buildSubAck(messageId, grantedQoSLevels);
                    connection.write(mqttSubAckMessage).subscribe();
                    RsocketTopicManager topicManager = serverConfig.getTopicManager();
                    subscribeMessage.payload().topicSubscriptions().stream().forEach(m -> {
                        String topic = m.topicName();
                        topicManager.addTopicConnection(topic, connection);
                        serverConfig.getMessageHandler().getRetain(topic)
                                .ifPresent(reatinMessages ->
                                        reatinMessages.forEach(reatinMessage -> {
                                            if (reatinMessage.getQos() == 0) {
                                                connection.write(MqttMessageApi.buildPub(reatinMessage.isDup(), MqttQoS.valueOf(reatinMessage.getQos()), reatinMessage.isRetain(), 1, reatinMessage.getTopicName(), Unpooled.wrappedBuffer(reatinMessage.getCopyByteBuf()))).subscribe();
                                            } else {
                                                int id = connection.messageId();
                                                connection.addDisposable(id, Mono.fromRunnable(() ->
                                                        connection.write(MqttMessageApi.buildPub(true, header.qosLevel(), header.isRetain(), id, reatinMessage.getTopicName(), Unpooled.wrappedBuffer(reatinMessage.getCopyByteBuf()))).subscribe())
                                                        .delaySubscription(Duration.ofSeconds(10)).repeat().subscribe()); // retry
                                                MqttPublishMessage publishMessage = MqttMessageApi.buildPub(false, header.qosLevel(), header.isRetain(), id, reatinMessage.getTopicName(), Unpooled.wrappedBuffer(reatinMessage.getCopyByteBuf())); // pub
                                                connection.write(publishMessage).subscribe();
                                            }
                                        }));

                    });

                    break;
                case UNSUBSCRIBE:
                    MqttUnsubscribeMessage mqttUnsubscribeMessage = (MqttUnsubscribeMessage) message;
                    MqttUnsubAckMessage mqttUnsubAckMessage = MqttMessageApi.buildUnsubAck(mqttUnsubscribeMessage.variableHeader().messageId());
                    connection.write(mqttUnsubAckMessage).subscribe();
                    mqttUnsubscribeMessage.payload().topics().stream().forEach(m -> serverConfig.getTopicManager().deleteTopicConnection(m, connection));
                    break;
            }
    }
}
