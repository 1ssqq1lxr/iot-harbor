package com.iot.transport.server.handler;

import com.iot.common.exception.NotSuppportHandlerException;
import com.iot.transport.DirectHandler;
import com.iot.transport.server.handler.connect.ConnectHandler;
import com.iot.transport.server.handler.heart.HeartHandler;
import com.iot.transport.server.handler.pub.PubHandler;
import com.iot.transport.server.handler.sub.SubHandler;
import io.netty.handler.codec.mqtt.MqttMessageType;

import java.util.concurrent.ConcurrentHashMap;

public class DirectHandlerFactory {

    private final  MqttMessageType messageType;

    private  ConcurrentHashMap<MqttMessageType, DirectHandler> messageTypeCollection = new ConcurrentHashMap();

    public DirectHandlerFactory(MqttMessageType messageType) {
        this.messageType = messageType;
    }

    public DirectHandler loadHandler(){
        return messageTypeCollection.computeIfAbsent(messageType,type->{
            switch (type){
                case PUBACK:
                case PUBREC:
                case PUBREL:
                case PUBLISH:
                case PUBCOMP:
                    return new PubHandler();

                case CONNECT:
                case DISCONNECT:
                    return new ConnectHandler();

                case PINGREQ:
                    return new HeartHandler();

                case SUBSCRIBE:
                case UNSUBSCRIBE:
                    return new SubHandler();
            }
            throw  new NotSuppportHandlerException(messageType+" not support ");
        });
    }


}
