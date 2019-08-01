package com.iot.api;

import reactor.core.Disposable;
import reactor.core.publisher.Mono;

public interface RsocketHandler {

    Mono<Void> onWriteIdle(long idleTimeout, Runnable onWriteIdle);


    Mono<Void>  onReadIdle(long idleTimeout, Runnable onWriteIdle);


    Mono<Void>  onClose(Disposable disposable);


    Mono<Void>  olose();






}
