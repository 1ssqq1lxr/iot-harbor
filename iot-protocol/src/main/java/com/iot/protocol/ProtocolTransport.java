package com.iot.protocol;

import com.iot.api.RsocketConfiguration;
import com.iot.common.connection.TransportConnection;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.netty.Connection;
import reactor.netty.DisposableServer;


public abstract class ProtocolTransport {

    protected Protocol protocol;

    public ProtocolTransport(Protocol protocol){
        this.protocol=protocol;
    }

    public abstract Mono<? extends DisposableServer> start(RsocketConfiguration config, UnicastProcessor<TransportConnection> connections);

    public abstract Mono<TransportConnection>  connect(RsocketConfiguration config);

}
