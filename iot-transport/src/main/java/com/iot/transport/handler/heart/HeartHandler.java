package com.iot.transport.handler.heart;

import com.iot.common.message.TransportMessage;
import com.iot.transport.handler.DirectHandler;
import reactor.core.publisher.Mono;

public class HeartHandler implements DirectHandler {
    @Override
    public Mono<Void> handler(TransportMessage message) {
        return null;
    }
}
