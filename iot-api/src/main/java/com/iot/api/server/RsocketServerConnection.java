package com.iot.api.server;


import com.iot.api.RsocketServerAbsOperation;
import com.iot.common.connection.TransportConnection;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.util.List;

public class RsocketServerConnection extends RsocketServerAbsOperation {




    public  RsocketServerConnection(UnicastProcessor<TransportConnection> connections){
        connections.subscribe();
    }

    @Override
    public Flux<TransportConnection> onConnect() {
        return null;
    }

    @Override
    public Mono<List<TransportConnection>> getConnections() {
        return null;
    }

    @Override
    public Mono<Void> closeConnect(String clientId) {
        return null;
    }

    @Override
    public Flux<TransportConnection> onClose() {
        return null;
    }


    @Override
    public Mono<Disposable> close() {
        return null;
    }

    @Override
    public Mono<Void> onClose(Disposable disposable) {
        return null;
    }
}
