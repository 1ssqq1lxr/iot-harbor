package com.iot.common.connection;

import lombok.Builder;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

@Builder
@Getter
public class TransportConnection {

    private NettyInbound inbound;

    private NettyOutbound outbound;

    private Connection connection;

    public <T> Flux<T> receive(Class<T> tClass){
        return  inbound.receive().cast(tClass);
    }


}
