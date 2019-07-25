package com.iot.protocol.mqtt;

import com.iot.api.ClientOperation;
import com.iot.api.Config;
import com.iot.common.connection.ServerConnection;
import com.iot.api.Transport;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;

import java.util.function.Consumer;

public class MqttTransport extends Transport {

    public MqttTransport(MqttProtocol mqttProtocol) {
        super(mqttProtocol);
    }


    @Override
    public Mono<DisposableServer> start(Config config, Consumer<ServerConnection> consumer) {
        return null;
    }

    @Override
    public Mono<ClientOperation> connect(Config config) {
        return null;
    }
}
