package com.iot.transport.server.handler;

import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketServerConfig;
import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.publisher.Mono;

public class AqsDirectHandler implements DirectHandler {

    public final DirectHandler directHandler;

    public AqsDirectHandler(DirectHandler directHandler) {
        this.directHandler = directHandler;
    }


    @Override
    public Mono<Void> handler(MqttMessage message, TransportConnection connection, RsocketServerConfig config) {
        return directHandler.handler(message, connection, config);
    }

     public  DirectHandlerAdaptor  load(){
        return  DirectHandlerFactory::new;
     }
}
