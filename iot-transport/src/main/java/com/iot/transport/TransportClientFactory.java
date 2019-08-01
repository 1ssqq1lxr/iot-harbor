package com.iot.transport;

import com.iot.config.ClientConfig;
import com.iot.protocol.ProtocolFactory;
import com.iot.common.annocation.ProtocolType;
import reactor.core.publisher.Mono;


public class TransportClientFactory {

    private ProtocolFactory protocolFactory;

    public TransportClientFactory(){
        protocolFactory = new ProtocolFactory();
    }


    public Mono<ClientOperation> connect(ClientConfig config) {
        return  Mono.from(protocolFactory.getProtocol(ProtocolType.valueOf(config.getProtocol()))
                .get().getTransport().connect(config));
    }
}
