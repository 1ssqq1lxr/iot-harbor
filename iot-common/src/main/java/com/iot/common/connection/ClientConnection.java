package com.iot.common.connection;

import com.iot.common.Qos;
import com.iot.common.message.TransportMessage;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public class ClientConnection implements  ClientOperation {


    private final  MessageConnection connection;

    public ClientConnection(MessageConnection connection) {
        this.connection = connection;
    }

    @Override
    public Mono<Disposable> close() {
        return null;
    }

    @Override
    public Mono<ClientOperation> connect() {
        return null;
    }

    @Override
    public Mono<Void> pub(String topic, String message) {
        return null;
    }

    @Override
    public Mono<Void> pub(String topic, String message, Qos qos) {
        return null;
    }

    @Override
    public <T> Mono<Void> sub(String topic, Consumer<TransportMessage<T>> consumer) {
        return null;
    }

    @Override
    public MessageConnection getConnection() {
        return connection;
    }


}
