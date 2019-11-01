package com.iot.transport.server.connection;


import com.iot.api.*;
import com.iot.api.server.RsocketServerSession;
import com.iot.api.server.handler.MemoryChannelManager;
import com.iot.api.server.handler.MemoryTopicManager;
import com.iot.api.TransportConnection;
import com.iot.config.RsocketServerConfig;
import com.iot.protocol.ws.WsProtocol;
import com.iot.protocol.ws.WsTransport;
import com.iot.transport.server.handler.ServerMessageRouter;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.Attribute;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.netty.Connection;
import reactor.netty.DisposableServer;
import reactor.netty.NettyInbound;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class RsocketServerConnection implements RsocketServerSession {


    private DisposableServer disposableServer;


    private RsocketChannelManager channelManager;

    private RsocketTopicManager topicManager;

    private RsocketMessageHandler rsocketMessageHandler;

    private RsocketServerConfig config;


    private ServerMessageRouter messageRouter;

    public RsocketServerConnection(UnicastProcessor<TransportConnection> connections, DisposableServer server, RsocketServerConfig config) {
        this.disposableServer = server;
        this.config = config;
        this.rsocketMessageHandler = config.getMessageHandler();
        this.topicManager = Optional.ofNullable(config.getTopicManager()).orElse(new MemoryTopicManager());
        this.channelManager = Optional.ofNullable(config.getChannelManager()).orElse(new MemoryChannelManager());
        this.messageRouter = new ServerMessageRouter(config);
        connections.subscribe(this::subscribe);

    }

    private void subscribe(TransportConnection connection) {
        NettyInbound inbound = connection.getInbound();
        Connection c = connection.getConnection();
        Disposable disposable = Mono.fromRunnable(c::dispose)// 定时关闭
                .delaySubscription(Duration.ofSeconds(10))
                .subscribe();
        c.channel().attr(AttributeKeys.connectionAttributeKey).set(connection); // 设置connection
        c.channel().attr(AttributeKeys.closeConnection).set(disposable);   // 设置close
        connection.getConnection().onReadIdle(config.getHeart(), () -> connection.getConnection().dispose()); // 心跳超时关闭
        connection.getConnection().onDispose(() -> { // 关闭  发送will消息
            Optional.ofNullable(connection.getConnection().channel().attr(AttributeKeys.WILL_MESSAGE)).map(Attribute::get)
                    .ifPresent(willMessage -> Optional.ofNullable(topicManager.getConnectionsByTopic(willMessage.getTopicName()))
                            .ifPresent(connections -> connections.forEach(co -> {
                                MqttQoS qoS = MqttQoS.valueOf(willMessage.getQos());
                                switch (qoS) {
                                    case AT_LEAST_ONCE:
                                        co.sendMessage(false, qoS, willMessage.isRetain(), willMessage.getTopicName(), willMessage.getCopyByteBuf()).subscribe();
                                        break;
                                    case EXACTLY_ONCE:
                                    case AT_MOST_ONCE:
                                        co.sendMessageRetry(false, qoS, willMessage.isRetain(), willMessage.getTopicName(), willMessage.getCopyByteBuf()).subscribe();
                                        break;
                                    default:
                                        co.sendMessage(false, qoS, willMessage.isRetain(), willMessage.getTopicName(), willMessage.getCopyByteBuf()).subscribe();
                                        break;
                                }
                            })));
            channelManager.removeConnections(connection); // 删除链接
            connection.getTopics().forEach(topic->topicManager.deleteTopicConnection(topic,connection)); // 删除topic订阅
        });
        inbound.receiveObject().cast(MqttMessage.class)
                .subscribe(message -> messageRouter.handler(message, connection));
        WsTransport wsTransport = new WsTransport(new WsProtocol());
        wsTransport.connect(config);
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
    public void dispose() {
        disposableServer.dispose();
    }
}
