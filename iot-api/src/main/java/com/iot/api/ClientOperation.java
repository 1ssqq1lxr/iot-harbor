package com.iot.api;


import com.iot.common.Qos;
import com.iot.common.connection.MessageConnection;
import com.iot.common.message.TransportMessage;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;


public interface ClientOperation {

    Mono<Disposable> close();

    Mono<ClientOperation> connect();

    Mono<Void> pub(String topic,String message);

    Mono<Void> pub(String topic, String message, Qos qos);

    <T> Mono<Void> sub(String topic, Consumer<TransportMessage> consumer);

    MessageConnection getConnection();

    Mono<Void> ping();

    Mono<Void> pong();

    Mono<Void> onClose(Consumer<ClientOperation> consumer);

}
