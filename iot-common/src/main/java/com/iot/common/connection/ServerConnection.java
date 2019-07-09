package com.iot.common.connection;


import reactor.core.Disposable;
import reactor.core.publisher.Mono;

public interface ServerConnection {

    Mono<Disposable> close();


}
