package com.iot.transport.handler;

import com.iot.common.exception.NotSuppportHandlerException;
import com.iot.protocol.mqtt.MqttProtocol;
import com.iot.transport.handler.connect.ConnectHandler;
import com.iot.transport.handler.heart.HeartHandler;
import com.iot.transport.handler.pub.PubHandler;
import com.iot.transport.handler.sub.SubHandler;
import io.netty.handler.codec.mqtt.MqttMessageType;

public class DirectHandlerFactory {

    private final  MqttMessageType messageType;


    public DirectHandlerFactory(MqttMessageType messageType) {
        this.messageType = messageType;
    }

    public  DirectHandler  loadHandler(){
        switch (messageType){
            case PUBACK:
                return new PubHandler();
            case PUBREC:
                return new PubHandler();
            case PUBREL:
                return new PubHandler();
            case SUBACK:
                return new SubHandler();
            case CONNACK:
                return new ConnectHandler();
            case PINGREQ:
                return new HeartHandler();
            case PUBCOMP:
                return new PubHandler();
            case PUBLISH:
                return new PubHandler();
            case PINGRESP:
                return new HeartHandler();
            case UNSUBACK:
                return new SubHandler();
            case SUBSCRIBE:
                return new SubHandler();
            case DISCONNECT:
                return new ConnectHandler();
            case UNSUBSCRIBE:
                return new SubHandler();
        }
        throw  new NotSuppportHandlerException(messageType+" not support ");
    }


}
