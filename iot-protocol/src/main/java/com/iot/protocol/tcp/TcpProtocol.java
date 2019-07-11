package com.iot.protocol.tcp;

import com.iot.protocol.Protocol;
import com.iot.protocol.ProtocolType;
import com.iot.protocol.Transport;
import com.iot.protocol.mqtt.MqttTransport;
import io.netty.channel.ChannelHandler;

public class TcpProtocol implements Protocol {
    @Override
    public ChannelHandler[] getChannelHandler() {
        return new ChannelHandler[0];
    }

    @Override
    public boolean support(ProtocolType protocolType) {
        return protocolType == ProtocolType.TCP;
    }

    @Override
    public Transport getTransport() {
        return new TcpTransport(this);
    }
}
