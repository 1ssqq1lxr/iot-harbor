package com.iot.protocol.tcp;

import com.iot.common.connection.ClientConnection;
import com.iot.common.connection.ClientOperation;
import com.iot.common.connection.MessageConnection;
import com.iot.common.connection.ServerOperation;
import com.iot.config.ClientConfig;
import com.iot.protocol.Protocol;
import com.iot.protocol.Transport;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpClient;

import java.util.Arrays;
import java.util.function.Consumer;

public class TcpTransport extends Transport {


    public TcpTransport(Protocol protocol) {
        super(protocol);
    }

    @Override
    public Mono<Disposable> start(Consumer<ServerOperation> consumer) {
        return null;
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
                        .build()) {
                });
    }



}
