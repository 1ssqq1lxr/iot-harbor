package com.iot.transport.connection;


import com.iot.api.RsocketChannelManager;
import com.iot.api.RsocketMessageHandler;
import com.iot.api.RsocketServerAbsOperation;
import com.iot.api.RsocketTopicManager;
import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.handler.MessageRouter;
import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.netty.DisposableServer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RsocketServerConnection extends RsocketServerAbsOperation {


    private DisposableServer disposableServer;


    private RsocketChannelManager channelManager;

    private RsocketTopicManager  topicManager;

    private RsocketMessageHandler rsocketMessageHandler;

    private RsocketServerConfig config;


    private ConcurrentHashMap<String,TransportConnection> connections= new ConcurrentHashMap<>();

    private MessageRouter messageRouter;

    public  RsocketServerConnection(UnicastProcessor<TransportConnection> connections, DisposableServer server, RsocketServerConfig config){
        this.disposableServer=server;
        this.config=config;
        this.rsocketMessageHandler=config.getMessageHandler();
        this.messageRouter= new MessageRouter();
        connections.subscribe(this::subscribe);

    }

    private void  subscribe(TransportConnection connection){
        connection.getInbound().receiveObject().cast(MqttMessage.class)
                .subscribe(messageRouter::handler);
    }

    @Override
    public Flux<TransportConnection> onConnect() {
        return null;
    }

    @Override
    public Mono<List<TransportConnection>> getConnections() {
        return null;
    }

    @Override
    public Mono<Void> closeConnect(String clientId) {
        return null;
    }

    @Override
    public Flux<TransportConnection> onClose() {
        return null;
    }


    @Override
    public Mono<Disposable> close() {
        return null;
    }

    @Override
    public Mono<Void> onClose(Disposable disposable) {
        return null;
    }

    @Override
    public void dispose() {
        disposableServer.dispose();
    }
}
