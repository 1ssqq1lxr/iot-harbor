package com.iot.protocol.mqtt;

import com.iot.common.connection.ClientConnection;
import com.iot.common.connection.ServerConnection;
import com.iot.protocol.Transport;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public class MqttTransport implements Transport {



    @Override
    public Mono<Disposable> start(Consumer<ServerConnection> consumer) {
        return null;
    }

    @Override
    public Mono<ClientConnection> connect(com.iot.config.ClientConfig config) {
        return null;
    }

}
