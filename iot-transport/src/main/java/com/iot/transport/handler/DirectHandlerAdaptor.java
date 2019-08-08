package com.iot.transport.handler;

import com.iot.common.message.TransportMessage;
import reactor.core.publisher.Mono;

public interface DirectHandlerAdaptor {

    Mono<Void> handler(TransportMessage message);

}
