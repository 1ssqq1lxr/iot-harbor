package com.iot.protocol;

import io.netty.channel.ChannelHandler;

import java.util.List;

public interface Protocol {

    List<ChannelHandler> getChannelHandler();

    boolean support(ProtocolType protocolType);

    Transport getTransport();

}
