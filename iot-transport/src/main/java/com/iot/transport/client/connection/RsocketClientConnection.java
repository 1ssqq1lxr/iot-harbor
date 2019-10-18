package com.iot.transport.client.connection;

import com.iot.api.client.RsocketClientSession;
import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketClientConfig;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;

import java.util.List;

public class RsocketClientConnection implements RsocketClientSession {

    private final TransportConnection connection;

    private final  RsocketClientConfig clientConfig;

    public RsocketClientConnection(TransportConnection connection, RsocketClientConfig clientConfig) {
        this.clientConfig=clientConfig;
        this.connection=connection;
    }

    @Override
    public Mono<Void> pub(String topic, String message, boolean retained, int qos) {
        return null;
    }

    @Override
    public Mono<Void> pub(String topic, String message) {
        return null;
    }

    @Override
    public Mono<Void> pub(String topic, String message, int qos) {
        return null;
    }

    @Override
    public Mono<Void> pub(String topic, String message, boolean retained) {
        return null;
    }

    @Override
    public Mono<Void> sub(String... subMessages) {
        return null;
    }

    @Override
    public Mono<Void> unsub(List<String> topics) {
        return null;
    }

    @Override
    public Mono<Void> unsub() {
        return null;
    }

    @Override
    public void dispose() {

    }
}
