package com.iot.transport.handler.pub;

import com.iot.common.message.TransportMessage;
import com.iot.transport.handler.DirectHandler;
import reactor.core.publisher.Mono;

public class PubHandler implements DirectHandler {
    @Override
    public Mono<Void> handler(TransportMessage message) {
        return null;
    }
}
