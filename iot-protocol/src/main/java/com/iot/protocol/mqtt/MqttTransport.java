package com.iot.protocol.mqtt;

import com.iot.api.RsocketClientAbsOperation;
import com.iot.api.RsocketConfiguration;
import com.iot.api.RsocketServerAbsOperation;
import com.iot.api.server.RsocketServerConnection;
import com.iot.common.codec.ProtocolCatagory;
import com.iot.common.message.TransportMessage;
import com.iot.protocol.ProtocolTransport;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

import java.time.Duration;

@Slf4j
public class MqttTransport extends ProtocolTransport {

    public MqttTransport(MqttProtocol mqttProtocol) {
        super(mqttProtocol);
    }


    @Override
    public Mono<? extends RsocketServerAbsOperation> start(RsocketConfiguration config) {


        return Mono.create(monoSink -> {
            return   buildServer(config)
                    .doOnConnection(connection -> {
                        connection.addHandler("decoder",new MqttDecoder()).addHandler("encoder",new MqttEncoder());
                        connection.onReadIdle(config.getHeart()*1000,()->connection.dispose());
                        Disposable disposable = Mono.fromRunnable(()->{
                            connection.dispose();
                            log.info("*************************************************************非法链接 未发送online报文  自动关闭");
                        }).delaySubscription(Duration.ofSeconds(10)).subscribe();

                    })
                    .bind()
                    .doOnError(error->log.error("************************************************************* server error {}",error.getMessage()))
                    .cast(DisposableServer.class)
                    .map(disposableServer -> {
                        serverConnection.setDisposableServer(disposableServer);
                        return  serverConnection;
                    });
        });
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
    public Mono<? extends RsocketClientAbsOperation> connect(RsocketConfiguration config) {
        return null;
    }




}
