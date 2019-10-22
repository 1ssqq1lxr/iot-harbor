package com.iot.common.connection;

import com.iot.common.message.TransportMessage;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
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


    public <T> Flux<T> receive(Class<T> tClass){
        return  inbound.receive().cast(tClass);
    }

    public  TransportConnection(Connection connection){
        this.connection=connection;
        this.inbound=connection.inbound();
        this.outbound=connection.outbound();
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


    public  TransportMessage  getAndRemoveQos2Message(Integer messageId){
        TransportMessage message  = qos2Message.get(messageId);
        qos2Message.remove(messageId);
        return message;
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



}
