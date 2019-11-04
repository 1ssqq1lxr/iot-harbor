package com.iot.api;

import com.iot.common.message.TransportMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.LongAdder;

@Getter
@Setter
@ToString
public class TransportConnection implements Disposable {

    private NettyInbound inbound;

    private NettyOutbound outbound;

    private Connection connection;

    private LongAdder longAdder = new LongAdder();

    private ConcurrentHashMap<Integer,Disposable> concurrentHashMap = new ConcurrentHashMap<>(); //

    private ConcurrentHashMap<Integer, TransportMessage> qos2Message = new ConcurrentHashMap<>();

    private List<String> topics   = new CopyOnWriteArrayList<>();


    public <T> Flux<T> receive(Class<T> tClass){
        return  inbound.receive().cast(tClass);
    }

    public  TransportConnection(Connection connection){
        this.connection=connection;
        this.inbound=connection.inbound();
        this.outbound=connection.outbound();
    }


    public void addTopic(String topic){
        topics.add(topic);
    }

    public void removeTopic(String topic){
        topics.remove(topic);
    }


    public Mono<Void> write(Object object){
      return outbound.sendObject(object).then();
    }



    public Mono<Void> sendPingReq(){
        return outbound.sendObject(new MqttMessage(new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0))).then();
    }


    public Mono<Void> sendPingRes(){
        return outbound.sendObject(new MqttMessage(new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0))).then();
    }


    public int messageId(){
        longAdder.increment();
        int value=longAdder.intValue();
        if(value==Integer.MAX_VALUE){
            longAdder.reset();
            longAdder.increment();
            return longAdder.intValue();
        }
        return value;
    }



    public  void  saveQos2Message(Integer messageId,TransportMessage message){
        qos2Message.put(messageId,message);
    }


    public Optional<TransportMessage>  getAndRemoveQos2Message(Integer messageId){
        TransportMessage message  = qos2Message.get(messageId);
        qos2Message.remove(messageId);
        return Optional.ofNullable(message);
    }

    public  boolean  containQos2Message(Integer messageId,byte[] bytes){
       return qos2Message.containsKey(messageId);
    }



    public  void  addDisposable(Integer messageId,Disposable disposable){
        concurrentHashMap.put(messageId,disposable);
    }


    public  void  cancleDisposable(Integer messageId){
        Optional.ofNullable(concurrentHashMap.get(messageId))
                .ifPresent(dispose->dispose.dispose());
        concurrentHashMap.remove(messageId);
    }



    @Override
    public void dispose() {
        connection.dispose();
    }

    public boolean isDispose(){
        return connection.isDisposed();
    }

    public  Mono<Void> sendMessage(boolean isDup, MqttQoS qoS, boolean isRetain, String topic, byte[] message){
        return this.write(MqttMessageApi.buildPub(isDup,qoS,isRetain,1,topic, Unpooled.wrappedBuffer(message)));
    }
    public   Mono<Void> sendMessageRetry(boolean isDup, MqttQoS qoS, boolean isRetain, String topic, byte[] message){
        int id = this.messageId();
        this.addDisposable(id, Mono.fromRunnable(() ->
                this.write(MqttMessageApi.buildPub(isDup,qoS,isRetain,id,topic, Unpooled.wrappedBuffer(message))).subscribe())
                .delaySubscription(Duration.ofSeconds(10)).repeat().subscribe()); // retry
        MqttPublishMessage publishMessage = MqttMessageApi.buildPub(isDup,qoS,isRetain,id,topic, Unpooled.wrappedBuffer(message)); // pub
       return this.write(publishMessage);
    }




}
