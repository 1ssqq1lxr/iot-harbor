package com.iot.api;

import reactor.core.publisher.Mono;



public abstract class Transport {

    protected Protocol protocol;

    public Transport(Protocol protocol){
        this.protocol=protocol;
    }

    public abstract Mono<? extends ServerOperation> start(Config config);

    public abstract Mono<? extends ClientOperation> connect(Config config);

}
