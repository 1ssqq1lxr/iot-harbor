package com.iot.transport.client.handler.sub;

import com.iot.api.RsocketConfiguration;
import com.iot.common.connection.TransportConnection;
import com.iot.transport.DirectHandler;
import io.netty.handler.codec.mqtt.*;
import reactor.core.publisher.Mono;


public class SubHandler implements DirectHandler {


    @Override
    public Mono<Void> handler(MqttMessage message, TransportConnection connection, RsocketConfiguration config) {
        return Mono.fromRunnable(()->{
            MqttFixedHeader header=  message.fixedHeader();
            MqttMessageIdVariableHeader mqttMessageIdVariableHeader =(MqttMessageIdVariableHeader) message.variableHeader();
            switch (header.messageType()){
                case  SUBACK:
                case UNSUBACK:
                    connection.cancleDisposable(mqttMessageIdVariableHeader.messageId());
                    break;
            }
        });
    }
}
