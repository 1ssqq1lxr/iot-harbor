package com.iot.protocol;

import com.iot.common.connection.ClientOperation;
import com.iot.common.connection.ServerConnection;
import com.iot.config.ClientConfig;
import com.iot.config.ServerConfig;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;

import java.util.function.Consumer;


public abstract class Transport {

    protected Protocol protocol;

    public Transport(Protocol protocol){
        this.protocol=protocol;
    }

    public abstract Mono<DisposableServer> start(ServerConfig config, Consumer<ServerConnection> consumer);

    public abstract Mono<ClientOperation> connect(ClientConfig config);

}
