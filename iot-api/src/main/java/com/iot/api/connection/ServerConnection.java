package com.iot.api.connection;

import com.iot.api.RsocketOperation;
import com.iot.common.Qos;
import com.iot.common.connection.MessageConnection;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public class ServerConnection implements RsocketOperation {


    private final MessageConnection connection;

    public ServerConnection(MessageConnection connection) {
        this.connection = connection;
    }

    @Override
    public Mono<Disposable> close() {
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
    public Mono<Void> ping() {
        return null;
    }

    @Override
    public Mono<Void> pong() {
        return null;
    }

    @Override
    public Mono<Void> onClose(Consumer<RsocketOperation> consumer) {
        return  Mono.fromRunnable(()->connection.getConnection().onDispose(()-> consumer.accept(this)));
    }

}
