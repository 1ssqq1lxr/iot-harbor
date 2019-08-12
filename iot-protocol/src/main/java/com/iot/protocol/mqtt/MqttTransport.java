package com.iot.protocol.mqtt;

import com.iot.api.RsocketClientAbsOperation;
import com.iot.api.RsocketConfiguration;
import com.iot.api.RsocketServerAbsOperation;
import com.iot.common.connection.TransportConnection;
import com.iot.protocol.ProtocolTransport;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.netty.Connection;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;


@Slf4j
public class MqttTransport extends ProtocolTransport {

    public MqttTransport(MqttProtocol mqttProtocol) {
        super(mqttProtocol);
    }


    @Override
    public Mono<? extends DisposableServer> start(RsocketConfiguration config, UnicastProcessor<TransportConnection> connections) {
        return  buildServer(config)
                .doOnConnection(connection -> connections.onNext(new TransportConnection(connection)))
                .bind()
                .doOnError(error->log.error("************************************************************* server error {}",error.getMessage()));
    }



    private TcpServer buildServer(RsocketConfiguration config){
        TcpServer server =TcpServer.create()
                .port(config.getPort())
                .wiretap(config.isLog())
                .host(config.getIp());
        try {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            SslContext sslServer = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            return  config.isSsl()?server.secure(sslContextSpec -> sslContextSpec.sslContext(sslServer)):server;
        }catch (Exception e){
            log.error("*******************************************************************ssl error: {}",e.getMessage());
            return  server;
        }
    }

    @Override
    public Mono<Connection> connect(RsocketConfiguration config) {
        return null;
    }




}
