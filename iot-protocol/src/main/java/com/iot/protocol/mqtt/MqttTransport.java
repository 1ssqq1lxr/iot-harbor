package com.iot.protocol.mqtt;

import com.iot.common.connection.ClientOperation;
import com.iot.common.connection.ServerOperation;
import com.iot.config.ClientConfig;
import com.iot.protocol.Transport;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public class MqttTransport extends Transport {

    public MqttTransport(MqttProtocol mqttProtocol) {
        super(mqttProtocol);
    }

    @Override
    public Mono<Disposable> start(Consumer<ServerOperation> consumer) {
        return null;
    }

    @Override
    public Mono<ClientOperation> connect(ClientConfig config) {
        return null;
    }


}
