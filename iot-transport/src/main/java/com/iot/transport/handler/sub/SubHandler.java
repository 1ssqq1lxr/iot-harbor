package com.iot.transport.handler.sub;

import com.iot.common.message.TransportMessage;
import com.iot.transport.handler.DirectHandler;
import reactor.core.publisher.Mono;

public class SubHandler implements DirectHandler {
    @Override
    public Mono<Void> handler(TransportMessage message) {
        return null;
    }
}
