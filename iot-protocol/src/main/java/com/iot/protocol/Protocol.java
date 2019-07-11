package com.iot.protocol;

import io.netty.channel.ChannelHandler;

public interface Protocol {

    ChannelHandler[] getChannelHandler();

    boolean support(ProtocolType protocolType);



}
