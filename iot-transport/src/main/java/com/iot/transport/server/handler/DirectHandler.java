package com.iot.transport.server.handler;

import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketServerConfig;
import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.publisher.Mono;

public interface DirectHandler {

    Mono<Void> handler(MqttMessage message, TransportConnection connection, RsocketServerConfig config);

}
