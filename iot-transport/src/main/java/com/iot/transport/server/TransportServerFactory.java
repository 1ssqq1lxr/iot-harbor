package com.iot.transport.server;

import com.iot.api.RsocketServerAbsOperation;
import com.iot.transport.server.connection.RsocketServerConnection;
import com.iot.common.annocation.ProtocolType;
import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketServerConfig;
import com.iot.protocol.ProtocolFactory;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.netty.DisposableServer;


public class TransportServerFactory {

    private ProtocolFactory protocolFactory;

    private UnicastProcessor<TransportConnection> unicastProcessor =UnicastProcessor.create();

    private RsocketServerConfig config;

    public TransportServerFactory(){
        protocolFactory = new ProtocolFactory();
    }


    public Mono<RsocketServerAbsOperation> connect(RsocketServerConfig config) {
        this.config =config;
        return  Mono.from(protocolFactory.getProtocol(ProtocolType.valueOf(config.getProtocol()))
                .get().getTransport().start(config,unicastProcessor)).map(this::wrapper);
    }

    private  RsocketServerAbsOperation wrapper(DisposableServer server){
        return  new RsocketServerConnection(unicastProcessor,server,config);
    }




}
