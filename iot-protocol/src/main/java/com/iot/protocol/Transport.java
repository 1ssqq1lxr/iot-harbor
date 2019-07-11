package com.iot.protocol;

import com.iot.common.connection.ClientConnection;
import com.iot.common.connection.ServerConnection;
import com.iot.config.ClientConfig;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;


public interface Transport {

    Mono<Disposable> start(Consumer<ServerConnection> consumer);

    Mono<ClientConnection> connect(ClientConfig config);

}
