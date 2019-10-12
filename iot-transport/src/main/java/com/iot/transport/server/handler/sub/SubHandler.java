package com.iot.transport.server.handler.sub;

import com.iot.api.MqttMessageApi;
import com.iot.api.RsocketTopicManager;
import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.server.handler.DirectHandler;
import io.netty.handler.codec.mqtt.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubHandler implements DirectHandler {


    @Override
    public Mono<Void> handler(MqttMessage message, TransportConnection connection, RsocketServerConfig config) {
        return Mono.fromRunnable(()->{
            MqttFixedHeader header=  message.fixedHeader();
            switch (header.messageType()){
                case SUBSCRIBE:
                    MqttSubscribeMessage subscribeMessage  =(MqttSubscribeMessage)message;
                    int messageId=subscribeMessage.variableHeader().messageId();
                    List<Integer> grantedQoSLevels= subscribeMessage.payload().topicSubscriptions().stream().map(m->m.qualityOfService().value()).collect(Collectors.toList());
                    MqttSubAckMessage mqttSubAckMessage=MqttMessageApi.buildSubAck(messageId,grantedQoSLevels);
                    connection.write(mqttSubAckMessage).subscribe();
                    RsocketTopicManager topicManager= config.getTopicManager();
                    subscribeMessage.payload().topicSubscriptions().stream().forEach(m-> topicManager.addTopicConnection(m.topicName(),connection));
                    break;
                case UNSUBSCRIBE:
                    MqttUnsubscribeMessage mqttUnsubscribeMessage =(MqttUnsubscribeMessage) message;
                    MqttUnsubAckMessage mqttUnsubAckMessage=MqttMessageApi.buildUnsubAck(mqttUnsubscribeMessage.variableHeader().messageId());
                    connection.write(mqttUnsubAckMessage).subscribe();
                    mqttUnsubscribeMessage.payload().topics().stream().forEach(m->config.getTopicManager().deleteTopicConnection(m,connection));
                    break;
            }
        });
    }
}
