package com.iot.protocol.mqtt;

import com.iot.common.connection.ClientOperation;
import com.iot.common.connection.ServerConnection;
import com.iot.common.connection.ServerOperation;
import com.iot.config.ClientConfig;
import com.iot.config.ServerConfig;
import com.iot.protocol.Transport;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;

import java.util.function.Consumer;

public class MqttTransport extends Transport {

    public MqttTransport(MqttProtocol mqttProtocol) {
        super(mqttProtocol);
    }


    @Override
    public Mono<DisposableServer> start(ServerConfig config, Consumer<ServerConnection> consumer) {
        return null;
    }

    @Override
    public Mono<ClientOperation> connect(ClientConfig config) {
        return null;
    }


}
