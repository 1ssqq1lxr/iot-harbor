package com.iot.api;


import com.iot.common.annocation.ProtocolType;
import io.netty.channel.ChannelHandler;

import java.util.List;

public interface Protocol {


    boolean support(ProtocolType protocolType);

    Transport getTransport();

    List<Class<? extends ChannelHandler>> getHandlers();

}
