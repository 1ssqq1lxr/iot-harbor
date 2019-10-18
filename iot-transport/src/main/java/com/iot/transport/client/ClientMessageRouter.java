package com.iot.transport.client;

import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketClientConfig;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.server.handler.DirectHandler;
import com.iot.transport.server.handler.DirectHandlerAdaptor;
import com.iot.transport.server.handler.DirectHandlerFactory;
import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.publisher.Mono;

public class ClientMessageRouter {


    private final RsocketClientConfig config;

    private final DirectHandlerAdaptor directHandlerAdaptor;

    public ClientMessageRouter(RsocketClientConfig config) {
        this.config=config;
        this.directHandlerAdaptor= DirectHandlerFactory::new;
    }

    public Mono<Void> handler(MqttMessage message, TransportConnection connection) {
        log.info("accept message {}",message);
        DirectHandler handler=directHandlerAdaptor.handler(message.fixedHeader().messageType()).loadHandler();
        return handler.handler(message,connection,config);
    }

}
