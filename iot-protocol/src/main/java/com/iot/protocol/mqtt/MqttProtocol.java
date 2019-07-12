package com.iot.protocol.mqtt;

import com.iot.protocol.Protocol;
import com.iot.protocol.ProtocolType;
import com.iot.protocol.Transport;
import io.netty.channel.ChannelHandler;

import java.util.List;

public class MqttProtocol implements Protocol {


    @Override
    public List<ChannelHandler> getChannelHandler() {
        return null;
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
