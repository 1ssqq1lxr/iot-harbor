package com.iot.common.session;


import reactor.core.Disposable;
import reactor.core.publisher.Mono;

public interface ClientSession {

    Mono<Disposable> close();

    Mono<ClientSession> connect();

}
