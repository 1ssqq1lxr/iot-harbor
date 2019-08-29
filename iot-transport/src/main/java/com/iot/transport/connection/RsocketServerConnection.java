package com.iot.transport.connection;


import com.iot.api.*;
import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.handler.MessageRouter;
import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.netty.Connection;
import reactor.netty.DisposableServer;
import reactor.netty.NettyInbound;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class RsocketServerConnection extends RsocketServerAbsOperation {


    private DisposableServer disposableServer;


    private RsocketChannelManager channelManager;

    private RsocketTopicManager  topicManager;

    private RsocketMessageHandler rsocketMessageHandler;

    private RsocketServerConfig config;


    private MessageRouter messageRouter;

    public  RsocketServerConnection(UnicastProcessor<TransportConnection> connections, DisposableServer server, RsocketServerConfig config){
        this.disposableServer=server;
        this.config=config;
        this.rsocketMessageHandler=config.getMessageHandler();
        this.topicManager = Optional.ofNullable(config.getTopicManager()).orElse(new MemoryTopicManager());
        this.channelManager= Optional.ofNullable(config.getChannelManager()).orElse(new MemoryChannelManager());
        this.messageRouter= new MessageRouter(config);
        connections.subscribe(this::subscribe);

    }

    private void  subscribe(TransportConnection connection){
        NettyInbound inbound=connection.getInbound();
        Connection c =connection.getConnection();
        Disposable disposable = Mono.fromRunnable(()-> c.dispose())// 定时关闭
                .delaySubscription(Duration.ofSeconds(10))
                .subscribe();
        c.channel().attr(AttributeKeys.connectionAttributeKey).set(connection); // 设置connection
        c.channel().attr(AttributeKeys.closeConnection).set(disposable);   // 设置close
        connection.getConnection().onReadIdle(config.getHeart(),()->connection.getConnection().dispose()); // 心跳超时关闭
        inbound.receiveObject().cast(MqttMessage.class)
                .subscribe(message->messageRouter.handler(message,connection).subscribe());
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
    public Mono<Void> onClose(Disposable disposable) {
        return null;
    }

    @Override
    public void dispose() {
        disposableServer.dispose();
    }
}
