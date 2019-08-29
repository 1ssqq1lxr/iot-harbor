package com.iot.common.connection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

@Getter
@Setter
@ToString
public class TransportConnection {

    private NettyInbound inbound;

    private NettyOutbound outbound;

    private Connection connection;

    public <T> Flux<T> receive(Class<T> tClass){
        return  inbound.receive().cast(tClass);
    }

    public  TransportConnection(Connection connection){
        this.connection=connection;
        this.inbound=connection.inbound();
        this.outbound=connection.outbound();
    }


    public Mono<Void> write(Object object){
      return outbound.sendObject(outbound).then();
    }

}
