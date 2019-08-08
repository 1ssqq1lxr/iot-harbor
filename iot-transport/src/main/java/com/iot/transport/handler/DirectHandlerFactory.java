package com.iot.transport.handler;

import com.iot.common.exception.NotSuppportHandlerException;
import com.iot.transport.handler.connect.ConnectHandler;
import com.iot.transport.handler.heart.HeartHandler;
import com.iot.transport.handler.pub.PubHandler;
import com.iot.transport.handler.sub.SubHandler;

public class DirectHandlerFactory {

    private final  HandlerType handlerTypel;


    public DirectHandlerFactory(HandlerType handlerTypel) {
        this.handlerTypel = handlerTypel;
    }

    public  DirectHandler  loadHandler(){
        switch (handlerTypel){
            case PUB:
                return  new PubHandler();
            case SUB:
                return new SubHandler();
            case CONNECT:
                return new ConnectHandler();
            case HEART:
                return new HeartHandler();
        }
        throw  new NotSuppportHandlerException(handlerTypel+" not support ");
    }


}
