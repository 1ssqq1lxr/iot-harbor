package com.iot.api;


import reactor.core.Disposable;
import reactor.core.publisher.Mono;


public interface RsocketOperation {

    Mono<Disposable> close();

    Mono<Void> onClose(Disposable disposable);



}
