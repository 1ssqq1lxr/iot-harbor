package com.iot.protocol.mqtt;

import com.iot.api.RsocketClientAbsOperation;
import com.iot.api.RsocketConfiguration;
import com.iot.api.RsocketServerAbsOperation;
import com.iot.common.connection.AttributeKeys;
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
                        RsocketServerAbsOperation serverAbsOperation = new RsocketServerConnection();
                        connection.addHandler("decoder",new MqttDecoder()).addHandler("encoder",new MqttEncoder());
                        connection.onReadIdle(config.getHeart()*1000,()->connection.dispose());
                        Disposable disposable = Mono.fromRunnable(()->{
                            connection.dispose();
                            log.info("*************************************************************非法链接 未发送online报文  自动关闭");
                        })// 定时关闭
                                .delaySubscription(Duration.ofSeconds(10))
                                .subscribe();
                        connection.inbound().receiveObject()
                                .cast(TransportMessage.class)
                                .subscribe(msg->{
                                    switch (msg.getCatagory()){
                                        case MSG:
                                            log.info("*************************************************************accept msg {}",msg);
                                            // 传递消息
                                            messages.onNext(msg);
                                            break;
                                        case PING:
                                            // 回复 pong
                                            log.info("*************************************************************accept ping {}",msg);
                                            connection.outbound().sendObject(TransportMessage.builder()
                                                    .catagory(ProtocolCatagory.PONG)
                                                    .build()).then().subscribe();
                                            break;
                                        case ONLINE:
                                            if(config.getAuth().apply(msg.getDeviceId())){
                                                log.info("*************************************************************accept online  deviceId {}",msg);

                                                connection.channel().attr(AttributeKeys.deviceId).set(msg.getDeviceId());
                                                // 存储设备 channel
                                                messageConnections.onNext(MessageConnection.builder()
                                                        .connection(connection)
                                                        .inbound(connection.inbound())
                                                        .outbound(connection.outbound())
                                                        .build());
                                            }
                                            // 取消关闭链接
                                            disposable.dispose();
                                            break;
                                    }
                                });
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
