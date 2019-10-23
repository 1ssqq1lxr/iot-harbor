package com.iot.transport.client.handler.heart;

import com.iot.api.RsocketConfiguration;
import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.DirectHandler;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class HeartHandler implements DirectHandler {


    @Override
    public Mono<Void> handler(MqttMessage message, TransportConnection connection, RsocketConfiguration config) {
        return Mono.fromRunnable(()->{
            switch (message.fixedHeader().messageType()){
                case PINGRESP:
                break;
            }
        });

    }
}
