package com.iot.transport.handler;

import com.iot.common.connection.TransportConnection;
import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.publisher.Mono;

public class AqsDirectHandler implements  DirectHandler {

    public final DirectHandler directHandler;

    public AqsDirectHandler(DirectHandler directHandler) {
        this.directHandler = directHandler;
    }


    @Override
    public Mono<Void> handler(MqttMessage message, TransportConnection connection) {
        return directHandler.handler(message, connection);
    }

     public  DirectHandlerAdaptor  load(){
        return  DirectHandlerFactory::new;
     }
}
