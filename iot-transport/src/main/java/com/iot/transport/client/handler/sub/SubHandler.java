package com.iot.transport.client.handler.sub;

import com.iot.api.RsocketConfiguration;
import com.iot.api.TransportConnection;
import com.iot.transport.DirectHandler;
import io.netty.handler.codec.mqtt.*;


public class SubHandler implements DirectHandler {


    @Override
    public void handler(MqttMessage message, TransportConnection connection, RsocketConfiguration config) {
            MqttFixedHeader header=  message.fixedHeader();
            MqttMessageIdVariableHeader mqttMessageIdVariableHeader =(MqttMessageIdVariableHeader) message.variableHeader();
            switch (header.messageType()){
                case  SUBACK:
                case UNSUBACK:
                    connection.cancleDisposable(mqttMessageIdVariableHeader.messageId());
                    break;
            }
    }
}
