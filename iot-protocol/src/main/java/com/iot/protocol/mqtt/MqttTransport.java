package com.iot.protocol.mqtt;

import com.iot.api.RsocketClientAbsOperation;
import com.iot.api.RsocketConfiguration;
import com.iot.api.RsocketServerAbsOperation;
import com.iot.protocol.ProtocolTransport;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MqttTransport extends ProtocolTransport {

    public MqttTransport(MqttProtocol mqttProtocol) {
        super(mqttProtocol);
    }


    @Override
    public Mono<? extends RsocketServerAbsOperation> start(RsocketConfiguration config) {
        return null;
    }

    @Override
    public Mono<? extends RsocketClientAbsOperation> connect(RsocketConfiguration config) {
        return null;
    }




}
