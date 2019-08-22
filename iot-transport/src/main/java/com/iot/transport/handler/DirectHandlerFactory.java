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
            case PUBREC:
            case PUBREL:
            case SUBACK:
            case CONNACK:
            case PINGREQ:
            case PUBCOMP:
            case PUBLISH:
            case PINGRESP:
            case UNSUBACK:
            case SUBSCRIBE:
            case DISCONNECT:
            case UNSUBSCRIBE:
        }
        throw  new NotSuppportHandlerException(messageType+" not support ");
    }


}
