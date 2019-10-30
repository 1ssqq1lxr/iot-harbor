package com.iot.transport;

import com.iot.api.RsocketConfiguration;
import com.iot.api.TransportConnection;
import io.netty.handler.codec.mqtt.MqttMessage;

public interface DirectHandler {

    void handler(MqttMessage message, TransportConnection connection, RsocketConfiguration config);

}
