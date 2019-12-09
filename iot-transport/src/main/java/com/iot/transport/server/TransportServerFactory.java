package com.iot.transport.server;

import com.iot.api.server.RsocketServerSession;
import com.iot.protocol.ws.WsProtocol;
import com.iot.protocol.ws.WsTransport;
import com.iot.transport.server.connection.RsocketServerConnection;
import com.iot.common.annocation.ProtocolType;
import com.iot.api.TransportConnection;
import com.iot.config.RsocketServerConfig;
import com.iot.protocol.ProtocolFactory;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.netty.DisposableServer;


public class TransportServerFactory {

    private ProtocolFactory protocolFactory;

    private UnicastProcessor<TransportConnection> unicastProcessor =UnicastProcessor.create();

    private RsocketServerConfig config;

    private DisposableServer ws_server;

    public TransportServerFactory(){
        protocolFactory = new ProtocolFactory();
    }


    public Mono<RsocketServerSession> start(RsocketServerConfig config) {
        this.config =config;
        if(config.getProtocol() == ProtocolType.MQTT.name()){ // 开启
            WsTransport wsTransport = new WsTransport(new WsProtocol());
            RsocketServerConfig wsConfig=copy(config);
            ws_server=wsTransport.start(wsConfig,unicastProcessor).block();
        }
        return  Mono.from(
                protocolFactory.getProtocol(ProtocolType.valueOf(config.getProtocol()))
                .get().getTransport()
                .start(config,unicastProcessor))
                .map(this::wrapper)
                .doOnError(config.getThrowableConsumer());
    }

    private RsocketServerConfig copy(RsocketServerConfig config) {
        RsocketServerConfig serverConfig = new RsocketServerConfig();
        serverConfig.setThrowableConsumer(config.getThrowableConsumer());
        serverConfig.setLog(config.isLog());
        serverConfig.setMessageHandler(config.getMessageHandler());
        serverConfig.setAuth(config.getAuth());
        serverConfig.setChannelManager(config.getChannelManager());
        serverConfig.setIp(config.getIp());
        serverConfig.setPort(8443);
        serverConfig.setSsl(config.isSsl());
        serverConfig.setProtocol(ProtocolType.WS_MQTT.name());
        serverConfig.setHeart(config.getHeart());
        serverConfig.setTopicManager(config.getTopicManager());
        serverConfig.setRevBufSize(config.getRevBufSize());
        serverConfig.setSendBufSize(config.getSendBufSize());
        serverConfig.setNoDelay(config.isNoDelay());
        serverConfig.setKeepAlive(config.isKeepAlive());
        return  serverConfig;
    }

    private  RsocketServerSession wrapper(DisposableServer server){
        return  new RsocketServerConnection(unicastProcessor,server,ws_server,config);
    }




}
