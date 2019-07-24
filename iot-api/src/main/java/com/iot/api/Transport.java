package com.iot.api;

import com.iot.common.connection.ServerConnection;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;

import java.util.function.Consumer;


public abstract class Transport {

    protected Protocol protocol;

    public Transport(Protocol protocol){
        this.protocol=protocol;
    }

    public abstract Mono<DisposableServer> start(Config config, Consumer<ServerConnection> consumer);

    public abstract Mono<ClientOperation> connect(Config config);

}
