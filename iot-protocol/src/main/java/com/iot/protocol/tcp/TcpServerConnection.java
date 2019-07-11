package com.iot.protocol.tcp;

import com.iot.common.Qos;
import com.iot.common.connection.MessageConnection;
import com.iot.common.connection.ServerConnection;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

public class TcpServerConnection implements ServerConnection {

    private MessageConnection messageConnection;

    public TcpServerConnection(MessageConnection messageConnection) {
        this.messageConnection = messageConnection;
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
}
