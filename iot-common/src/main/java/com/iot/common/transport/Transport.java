package com.iot.common.transport;

import com.iot.common.connection.ClientConnection;
import com.iot.common.connection.ServerConnection;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;


public interface Transport {

    Mono<Disposable> start(Consumer<ServerConnection> consumer);

    Mono<ClientConnection> connect();

}
