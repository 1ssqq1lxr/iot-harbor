package com.iot.api.server;


import com.iot.api.RsocketMessageHandler;

public interface RsocketMqttServerAdaptor {

    RsocketMqttServerTransport transport(RsocketMessageHandler messageHandler);

}
