package com.iot.protocol;

import com.iot.api.RsocketClientAbsOperation;
import com.iot.api.RsocketConfiguration;
import com.iot.api.RsocketServerAbsOperation;
import reactor.core.publisher.Mono;



public abstract class ProtocolTransport {

    protected Protocol protocol;

    public ProtocolTransport(Protocol protocol){
        this.protocol=protocol;
    }

    public abstract Mono<? extends RsocketServerAbsOperation> start(RsocketConfiguration config);

    public abstract Mono<? extends RsocketClientAbsOperation> connect(RsocketConfiguration config);

}
