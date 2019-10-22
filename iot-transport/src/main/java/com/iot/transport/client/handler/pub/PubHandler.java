package com.iot.transport.client.handler.pub;

import com.iot.api.RsocketConfiguration;
import com.iot.common.connection.TransportConnection;
import com.iot.transport.DirectHandler;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Slf4j
public class PubHandler implements DirectHandler {


    @Override
    public Mono<Void> handler(MqttMessage message, TransportConnection connection, RsocketConfiguration config) {
        return Mono.fromRunnable(() -> {

        });
    }


}
