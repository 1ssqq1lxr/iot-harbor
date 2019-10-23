package com.iot.transport.server.handler;

import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.DirectHandler;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Getter
@Slf4j
public class ServerMessageRouter {


    private final RsocketServerConfig config;

    private final DirectHandlerAdaptor directHandlerAdaptor;

    public ServerMessageRouter(RsocketServerConfig config) {
        this.config=config;
        this.directHandlerAdaptor= DirectHandlerFactory::new;
    }

    public Mono<Void> handler(MqttMessage message, TransportConnection connection) {
        if(message.decoderResult().isSuccess()){
            DirectHandler handler=directHandlerAdaptor.handler(message.fixedHeader().messageType()).loadHandler();
            return handler.handler(message,connection,config);
        }
        else {
            log.error("accept message  error{}",message.decoderResult().toString());
            return Mono.empty();
        }
    }

}
