package com.iot.protocol.tcp;

import com.iot.protocol.Protocol;
import com.iot.protocol.ProtocolType;
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
}
