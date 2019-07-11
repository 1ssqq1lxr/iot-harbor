package com.iot.protocol;

import com.iot.common.connection.ClientConnection;
import com.iot.common.connection.ServerConnection;
import com.iot.config.ClientConfig;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;


public abstract class Transport {

    protected Protocol protocol;

    public Transport(Protocol protocol){
        this.protocol=protocol;
    }

    public abstract Mono<Disposable> start(Consumer<ServerConnection> consumer);

    public abstract Mono<ClientConnection> connect(ClientConfig config);

}
