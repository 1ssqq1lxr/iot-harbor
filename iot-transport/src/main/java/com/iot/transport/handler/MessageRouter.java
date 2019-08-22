package com.iot.transport.handler;

import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.publisher.Mono;

public class MessageRouter  {




    public Mono<Void> handler(MqttMessage message) {
        DirectHandlerAdaptor  handlerAdaptor= handlerAdaptor();
        DirectHandler handler=handlerAdaptor.handler(message.fixedHeader().messageType()).loadHandler();
        return handler.handler(message);
    }


    private  DirectHandlerAdaptor  handlerAdaptor(){
        return DirectHandlerFactory::new;
    }

}
