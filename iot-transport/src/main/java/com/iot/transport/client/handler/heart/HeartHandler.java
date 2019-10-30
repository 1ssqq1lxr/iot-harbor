package com.iot.transport.client.handler.heart;

import com.iot.api.RsocketConfiguration;
import com.iot.api.TransportConnection;
import com.iot.transport.DirectHandler;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartHandler implements DirectHandler {


    @Override
    public void handler(MqttMessage message, TransportConnection connection, RsocketConfiguration config) {
            switch (message.fixedHeader().messageType()){
                case PINGRESP:
                break;
            }

    }
}
