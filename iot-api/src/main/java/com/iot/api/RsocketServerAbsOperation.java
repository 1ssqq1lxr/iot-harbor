package com.iot.api;


import com.iot.common.connection.TransportConnection;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public abstract class RsocketServerAbsOperation implements  RsocketOperation, Disposable {


    public  abstract  Mono<List<TransportConnection>> getConnections();

    public  abstract  Mono<Void> closeConnect(String clientId);




}
