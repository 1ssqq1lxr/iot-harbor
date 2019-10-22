package com.iot.transport;


import io.netty.handler.codec.mqtt.MqttMessageType;

public interface DirectHandlerAdaptor {

    DirectHandlerFactory handler(MqttMessageType messageType);

}
