package com.iot.common.session;


import reactor.core.Disposable;
import reactor.core.publisher.Mono;

public interface ServerSession {

    Mono<Disposable> close();

    Mono<ServerSession> start();

}
