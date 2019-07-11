package com.iot.protocol.tcp;

import com.iot.common.connection.ClientConnection;
import com.iot.common.connection.ServerConnection;
import com.iot.config.ClientConfig;
import com.iot.protocol.Protocol;
import com.iot.protocol.Transport;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public class TcpTransport extends Transport {


    public TcpTransport(Protocol protocol) {
        super(protocol);
    }

    @Override
    public Mono<Disposable> start(Consumer<ServerConnection> consumer) {
        return null;
    }

    @Override
    public Mono<ClientConnection> connect(ClientConfig config) {
        return null;
    }



}
