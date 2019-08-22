package com.iot.transport.handler;

import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.publisher.Mono;

public class AqsDirectHandler implements  DirectHandler {

    public final DirectHandler directHandler;

    public AqsDirectHandler(DirectHandler directHandler) {
        this.directHandler = directHandler;
    }


    @Override
    public Mono<Void> handler(MqttMessage message) {
        return directHandler.handler(message);
    }

     public  DirectHandlerAdaptor  load(){
        return  DirectHandlerFactory::new;
     }
}
