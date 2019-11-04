package com.iot.transport.client.connection;

import com.google.common.collect.Lists;
import com.iot.api.AttributeKeys;
import com.iot.api.MqttMessageApi;
import com.iot.api.client.RsocketClientSession;
import com.iot.api.TransportConnection;
import com.iot.config.RsocketClientConfig;
import com.iot.transport.client.handler.ClientMessageRouter;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class RsocketClientConnection implements RsocketClientSession {

    private final TransportConnection connection;

    private final RsocketClientConfig clientConfig;

    private  ClientMessageRouter clientMessageRouter;

    private List<String> topics = Lists.newArrayList();

    public RsocketClientConnection(TransportConnection connection, RsocketClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.connection = connection;
        this.clientMessageRouter = new ClientMessageRouter(clientConfig);
        initHandler();
    }

    public void  initHandler(){
        RsocketClientConfig.Options options = clientConfig.getOptions();
        NettyInbound inbound = connection.getInbound();
        Disposable disposable=Mono.fromRunnable(() -> connection.write(MqttMessageApi.buildConnect(
                options.getClientIdentifier(),
                options.getWillTopic(),
                options.getWillMessage(),
                options.getUserName(),
                options.getPassword(),
                options.isHasUserName(),
                options.isHasPassword(),
                options.isHasWillFlag(),
                options.getWillQos()
        )).subscribe()).delaySubscription(Duration.ofSeconds(2)).repeat().subscribe();
        connection.getConnection().channel().attr(AttributeKeys.closeConnection).set(disposable);
        connection.getConnection().onWriteIdle(clientConfig.getHeart(), () -> connection.sendPingReq().subscribe()); // 发送心跳
        connection.getConnection().onReadIdle(clientConfig.getHeart()*2, () -> connection.sendPingReq().subscribe()); // 发送心跳
        connection.getConnection().onDispose(()->clientConfig.getOnClose().run());
        inbound.receiveObject().cast(MqttMessage.class)
                .subscribe(message ->  clientMessageRouter.handler(message, connection));
        connection.getConnection().channel().attr(AttributeKeys.clientConnectionAttributeKey).set(this);
    }

    @Override
    public Mono<Void> pub(String topic, byte[] message, boolean retained, int qos) {
        int messageId = qos == 0 ? 1 : connection.messageId();
        MqttQoS mqttQoS = MqttQoS.valueOf(qos);
        switch (mqttQoS) {
            case AT_MOST_ONCE:
                return connection.write(MqttMessageApi.buildPub(false, MqttQoS.AT_MOST_ONCE, retained, messageId, topic, Unpooled.wrappedBuffer(message)));
            case EXACTLY_ONCE:
            case AT_LEAST_ONCE:
                return Mono.fromRunnable(()->{
                    connection.write(MqttMessageApi.buildPub(false, mqttQoS, retained, messageId, topic, Unpooled.wrappedBuffer(message))).subscribe();
                    connection.addDisposable(messageId, Mono.fromRunnable(() ->
                            connection.write(MqttMessageApi.buildPub(true,mqttQoS, retained, messageId, topic, Unpooled.wrappedBuffer(message))).subscribe())
                            .delaySubscription(Duration.ofSeconds(10)).repeat().subscribe()); // retry
                });
            default:
                return  Mono.empty();
        }
    }

    @Override
    public Mono<Void> pub(String topic, byte[] message) {
        return pub(topic, message, false, 0);
    }

    @Override
    public Mono<Void> pub(String topic, byte[] message, int qos) {
        return pub(topic, message, false, qos);
    }

    @Override
    public Mono<Void> pub(String topic, byte[] message, boolean retained) {
        return pub(topic, message, retained, 0);
    }


    @Override
    public Mono<Void> sub(String... subMessages) {
        topics.addAll(Arrays.asList(subMessages));
        List<MqttTopicSubscription> topicSubscriptions = Arrays.stream(subMessages).map(s -> new MqttTopicSubscription(s, MqttQoS.AT_MOST_ONCE)).collect(Collectors.toList());
        int messageId = connection.messageId();
        connection.addDisposable(messageId, Mono.fromRunnable(() ->
                connection.write(MqttMessageApi.buildSub(messageId, topicSubscriptions)).subscribe())
                .delaySubscription(Duration.ofSeconds(10)).repeat().subscribe()); // retry
        return connection.write(MqttMessageApi.buildSub(messageId, topicSubscriptions));
    }

    @Override
    public Mono<Void> unsub(List<String> topics) {
        this.topics.removeAll(topics);
        int messageId = connection.messageId();
        connection.addDisposable(messageId, Mono.fromRunnable(() ->
                connection.write(MqttMessageApi.buildUnSub(messageId, topics)).subscribe())
                .delaySubscription(Duration.ofSeconds(10)).repeat().subscribe()); // retry
        return connection.write(MqttMessageApi.buildUnSub(messageId, topics));
    }

    @Override
    public Mono<Void> unsub() {
        return unsub(this.topics);
    }

    @Override
    public Mono<Void> messageAcceptor(BiConsumer<String, byte[]> messageAcceptor) {
        return Mono.fromRunnable(() -> clientConfig.setMessageAcceptor(messageAcceptor));
    }

    @Override
    public void dispose() {
        connection.dispose();
    }
}
