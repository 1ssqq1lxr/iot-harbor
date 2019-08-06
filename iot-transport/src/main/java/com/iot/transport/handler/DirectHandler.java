package com.iot.transport.handler;

import com.iot.common.message.TransportMessage;
import reactor.core.publisher.Mono;

public interface DirectHandler {

    Mono<Void> handler(TransportMessage message);

}
