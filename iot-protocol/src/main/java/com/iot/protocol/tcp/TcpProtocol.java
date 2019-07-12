package com.iot.protocol.tcp;

import com.iot.protocol.Protocol;
import com.iot.protocol.ProtocolType;
import com.iot.protocol.Transport;
import io.netty.channel.ChannelHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TcpProtocol implements Protocol {



    private List<ChannelHandler> channelHandlers = new ArrayList<>();

    @Override
    public List<ChannelHandler> getChannelHandler() {
        return channelHandlers;
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
