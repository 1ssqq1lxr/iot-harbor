package com.iot.transport.server;

import com.iot.api.RsocketServerAbsOperation;
import com.iot.common.annocation.ProtocolType;
import com.iot.config.RsocketServerConfig;
import com.iot.protocol.ProtocolFactory;
import reactor.core.publisher.Mono;


public class TransportServerFactory {

    private ProtocolFactory protocolFactory;

    public TransportServerFactory(){
        protocolFactory = new ProtocolFactory();
    }


    public Mono<RsocketServerAbsOperation> connect(RsocketServerConfig config) {
        return  Mono.from(protocolFactory.getProtocol(ProtocolType.valueOf(config.getProtocol()))
                .get().getTransport().start(config));
    }
}
