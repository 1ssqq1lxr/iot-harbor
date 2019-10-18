package com.iot.transport.client;

import com.iot.api.client.RsocketClientAbsOperation;
import com.iot.config.RsocketClientConfig;
import com.iot.protocol.ProtocolFactory;
import com.iot.common.annocation.ProtocolType;
import reactor.core.publisher.Mono;


public class TransportClientFactory {

    private ProtocolFactory protocolFactory;

    public TransportClientFactory(){
        protocolFactory = new ProtocolFactory();
    }

//
//    public Mono<RsocketClientAbsOperation> connect(RsocketClientConfig config) {
//        return  Mono.from(protocolFactory.getProtocol(ProtocolType.valueOf(config.getProtocol()))
//                .get().getTransport().connect(config));
//    }
}
