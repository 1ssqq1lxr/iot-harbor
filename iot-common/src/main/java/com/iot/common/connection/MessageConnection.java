package com.iot.common.connection;

import lombok.Builder;
import lombok.Getter;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

@Builder
@Getter
public class MessageConnection {

    private NettyInbound inbound;

    private NettyOutbound outbound;

    private Connection connection;


}
