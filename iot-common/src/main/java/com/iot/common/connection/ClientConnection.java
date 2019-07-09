package com.iot.common.connection;


import reactor.core.Disposable;
import reactor.core.publisher.Mono;

public interface ClientConnection {

    Mono<Disposable> close();

    Mono<ClientConnection> connect();

}
