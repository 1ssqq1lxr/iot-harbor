package com.iot.api;


import reactor.core.Disposable;
import reactor.core.publisher.Mono;


public interface RsocketOperation {

    Mono<Void> onClose(Disposable disposable);



}
