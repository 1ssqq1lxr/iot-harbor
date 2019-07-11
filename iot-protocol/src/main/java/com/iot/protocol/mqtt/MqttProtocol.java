package com.iot.protocol.mqtt;

import com.iot.protocol.Protocol;
import com.iot.protocol.ProtocolType;
import com.iot.protocol.Transport;
import io.netty.channel.ChannelHandler;

public class MqttProtocol implements Protocol {

    @Override
    public ChannelHandler[] getChannelHandler() {
        return new ChannelHandler[0];
    }

    @Override
    public boolean support(ProtocolType protocolType) {
        return protocolType == ProtocolType.MQTT;
    }

    @Override
    public Transport getTransport() {
        return  new MqttTransport(this);
    }
}
