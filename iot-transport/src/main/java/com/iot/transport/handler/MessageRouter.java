package com.iot.transport.handler;

import com.iot.api.RsocketChannelManager;
import com.iot.api.RsocketMessageHandler;
import com.iot.api.RsocketTopicManager;
import com.iot.common.connection.TransportConnection;
import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.publisher.Mono;

public class MessageRouter  {

    private  RsocketTopicManager topicManager;

    private  RsocketMessageHandler rsocketMessageHandler;

    private  RsocketChannelManager channelManager;

    public MessageRouter(RsocketTopicManager topicManager, RsocketMessageHandler rsocketMessageHandler, RsocketChannelManager channelManager) {
        this.topicManager=topicManager;
        this.rsocketMessageHandler=rsocketMessageHandler;
        this.channelManager=channelManager;
    }

    public Mono<Void> handler(MqttMessage message, TransportConnection connection) {
        DirectHandlerAdaptor  handlerAdaptor= handlerAdaptor();
        DirectHandler handler=handlerAdaptor.handler(message.fixedHeader().messageType()).loadHandler();
        return handler.handler(message);
    }


    private  DirectHandlerAdaptor  handlerAdaptor(){
        return DirectHandlerFactory::new;
    }
}
