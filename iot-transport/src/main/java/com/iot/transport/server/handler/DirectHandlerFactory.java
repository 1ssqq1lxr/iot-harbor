package com.iot.transport.server.handler;

import com.iot.common.exception.NotSuppportHandlerException;
import com.iot.transport.server.handler.connect.ConnectHandler;
import com.iot.transport.server.handler.heart.HeartHandler;
import com.iot.transport.server.handler.pub.PubHandler;
import com.iot.transport.server.handler.sub.SubHandler;
import io.netty.handler.codec.mqtt.MqttMessageType;

public class DirectHandlerFactory {

    private final  MqttMessageType messageType;


    public DirectHandlerFactory(MqttMessageType messageType) {
        this.messageType = messageType;
    }

    public DirectHandler loadHandler(){
        switch (messageType){
            case PUBACK:
            case PUBREC:
            case PUBREL:
            case PUBLISH:
            case PUBCOMP:
                return new PubHandler();

            case CONNACK:
            case CONNECT:
            case DISCONNECT:
                return new ConnectHandler();

            case PINGREQ:
            case PINGRESP:
                return new HeartHandler();

            case UNSUBACK:
            case SUBSCRIBE:
            case UNSUBSCRIBE:
            case SUBACK:
                return new SubHandler();
        }
        throw  new NotSuppportHandlerException(messageType+" not support ");
    }


}
