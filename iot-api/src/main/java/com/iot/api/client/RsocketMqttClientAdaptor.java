package com.iot.api.client;


import com.iot.api.RsocketMessageHandler;

public interface RsocketMqttClientAdaptor {

    RsocketMqttClientTransport transport(RsocketMessageHandler messageHandler);

}
