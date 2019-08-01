package com.iot.api;


import com.iot.common.Qos;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public interface RsocketOperation {

    Mono<Disposable> close();

    Mono<Void> pub(String topic,String message);

    Mono<Void> pub(String topic, String message, Qos qos);

    Mono<Void> ping();

    Mono<Void> pong();

    Mono<Void> onClose(Consumer<RsocketOperation> consumer);
}
