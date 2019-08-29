package com.iot.transport.handler;

import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketServerConfig;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.Getter;
import reactor.core.publisher.Mono;

@Getter
public class MessageRouter  {


    private final RsocketServerConfig config;

    public MessageRouter(RsocketServerConfig config) {
        this.config=config;
    }

    public Mono<Void> handler(MqttMessage message, TransportConnection connection) {
        DirectHandlerAdaptor  handlerAdaptor= handlerAdaptor();
        DirectHandler handler=handlerAdaptor.handler(message.fixedHeader().messageType()).loadHandler();
        return handler.handler(message,connection,config);
    }


    private  DirectHandlerAdaptor  handlerAdaptor(){
        return DirectHandlerFactory::new;
    }
}
