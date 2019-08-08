package com.iot.api;


import com.iot.common.connection.TransportConnection;
import com.iot.common.message.TransportMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;

public abstract class RsocketServerAbsOperation implements  RsocketOperation{

    public  abstract Mono<Void> handler(Consumer<TransportMessage> transportMessage);

    public  abstract Flux<TransportConnection> onConnect();

    public  abstract  Mono<List<TransportConnection>> getConnections();

    public  abstract  Mono<Void> closeConnect(String clientId);

    public  abstract   Flux<TransportConnection>   onClose();

    public  abstract  Mono<Void>  plugins();


}
