package com.iot.transport;

import com.iot.api.RsocketConfiguration;
import com.iot.common.connection.TransportConnection;
import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.publisher.Mono;

public interface DirectHandler {

    Mono<Void> handler(MqttMessage message, TransportConnection connection, RsocketConfiguration config);

}
