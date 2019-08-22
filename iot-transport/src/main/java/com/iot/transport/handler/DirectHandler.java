package com.iot.transport.handler;

import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.publisher.Mono;

public interface DirectHandler {

    Mono<Void> handler(MqttMessage message);

}
