package com.iot.transport.handler.connect;

import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.handler.DirectHandler;
import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.publisher.Mono;

public class ConnectHandler implements DirectHandler {


    @Override
    public Mono<Void> handler(MqttMessage message, TransportConnection connection, RsocketServerConfig config) {
        return null;
    }
}
