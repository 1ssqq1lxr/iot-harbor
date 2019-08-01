package com.iot.protocol;

import com.iot.api.ClientOperation;
import com.iot.api.Config;
import com.iot.api.RsocketOperation;
import reactor.core.publisher.Mono;



public abstract class ProtocolTransport {

    protected Protocol protocol;

    public ProtocolTransport(Protocol protocol){
        this.protocol=protocol;
    }

    public abstract Mono<? extends RsocketOperation> start(Config config);

    public abstract Mono<? extends ClientOperation> connect(Config config);

}
