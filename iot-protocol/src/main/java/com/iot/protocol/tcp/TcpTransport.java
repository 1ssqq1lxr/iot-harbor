package com.iot.protocol.tcp;

import com.iot.common.connection.*;
import com.iot.config.ClientConfig;
import com.iot.config.ServerConfig;
import com.iot.protocol.Protocol;
import com.iot.protocol.Transport;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpClient;
import reactor.netty.tcp.TcpServer;

import java.util.Arrays;
import java.util.function.Consumer;

public class TcpTransport extends Transport {


    public TcpTransport(Protocol protocol) {
        super(protocol);
    }


    @Override
    public Mono<DisposableServer> start(ServerConfig config, Consumer<ServerConnection> consumer) {
        return TcpServer.create()
                .doOnConnection(c -> Arrays.asList(protocol.getChannelHandler()).forEach(channelHandler -> c.addHandler(channelHandler)) )
                .port(config.getPort())
                .wiretap(config.isLog())
                .host(config.getIp())
                .handle(((nettyInbound, nettyOutbound) -> {
                    nettyInbound.withConnection(connection ->
                        consumer.accept(new ServerConnection(MessageConnection.builder()
                                .outbound(nettyOutbound)
                                .inbound(nettyInbound)
                                .connection(connection)
                                .build())));
                    return Flux.never();
                }))
                .bind()
                .cast(DisposableServer.class);

    }

    @Override
    public Mono<ClientOperation> connect(ClientConfig config) {
        return   TcpClient.create()
                .port(config.getPort())
                .host(config.getIp())
                .doOnConnected(connection -> Arrays.asList(protocol.getChannelHandler()).forEach(channelHandler -> connection.addHandler(channelHandler)))
                .wiretap(config.isLog())
                .connect()
                .map(connection -> new ClientConnection(MessageConnection.builder()
                        .connection(connection)
                        .inbound(connection.inbound())
                        .outbound(connection.outbound())
                        .build()));
    }



}
