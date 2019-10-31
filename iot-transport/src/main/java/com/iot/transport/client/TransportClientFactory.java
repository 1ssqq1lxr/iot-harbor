package com.iot.transport.client;

import com.iot.api.client.RsocketClientSession;
import com.iot.common.annocation.ProtocolType;
import com.iot.api.TransportConnection;
import com.iot.config.RsocketClientConfig;
import com.iot.transport.client.connection.RsocketClientConnection;
import reactor.core.publisher.Mono;
import com.iot.protocol.ProtocolFactory;


public class TransportClientFactory {

    private ProtocolFactory protocolFactory;


    private RsocketClientConfig clientConfig;


    public TransportClientFactory(){
        protocolFactory = new ProtocolFactory();
    }


    public Mono<RsocketClientSession> connect(RsocketClientConfig config) {
        this.clientConfig=config;
        return  Mono.from(protocolFactory.getProtocol(ProtocolType.valueOf(config.getProtocol()))
                .get().getTransport().connect(config)).map(this::wrapper).doOnError(config.getThrowableConsumer());
    }


    private RsocketClientSession wrapper(TransportConnection connection){
        return  new RsocketClientConnection(connection,clientConfig);
    }

}
