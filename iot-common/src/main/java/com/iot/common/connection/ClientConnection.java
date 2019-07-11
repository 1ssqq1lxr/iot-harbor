package com.iot.common.connection;


import com.iot.common.Qos;
import com.iot.common.message.TransportMessage;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public interface ClientConnection {

    Mono<Disposable> close();

    Mono<ClientConnection> connect();

    Mono<Void> pub(String topic,String message);

    Mono<Void> pub(String topic, String message, Qos qos);

    <T> Mono<Void> sub(String topic, Consumer<TransportMessage<T>> consumer);


}
