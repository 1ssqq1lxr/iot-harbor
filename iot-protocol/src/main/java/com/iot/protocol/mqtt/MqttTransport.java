package com.iot.protocol.mqtt;

import com.iot.api.ClientOperation;
import com.iot.api.Config;
import com.iot.api.RsocketOperation;
import com.iot.api.connection.ClientConnection;
import com.iot.common.codec.ProtocolCatagory;
import com.iot.common.connection.AttributeKeys;
import com.iot.common.connection.MessageConnection;
import com.iot.common.connection.ServerConnection;
import com.iot.common.message.TransportMessage;
import com.iot.config.ClientConfig;
import com.iot.protocol.ProtocolTransport;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpClient;
import reactor.netty.tcp.TcpServer;

import java.time.Duration;

@Slf4j
public class MqttTransport extends ProtocolTransport {

    public MqttTransport(MqttProtocol mqttProtocol) {
        super(mqttProtocol);
    }



    @Override
    public Mono<? extends RsocketOperation> start(Config config) {

        return   buildServer(config)
                .doOnConnection(connection -> {

                })
                .bind()
                .doOnError(error->log.error("************************************************************* server error {}",error.getMessage()))
                .cast(DisposableServer.class)
                .map(disposableServer -> {
//                    serverConnection.setDisposableServer(disposableServer);
//                    return  serverConnection;
                    return  null;
                });
    }


    @Override
    public Mono<? extends ClientOperation> connect(Config config) {
        return  Mono.just(buildClient(config)
                .connectNow())
                .map(connection -> {
                    UnicastProcessor<TransportMessage>  messages = UnicastProcessor.create();
                    connection.addHandler("decoder",new MessageDecoder2()).addHandler("encoder",new MessageEncoder());
                    connection.outbound().sendObject(
                            TransportMessage.builder()
                                    .catagory(ProtocolCatagory.ONLINE)
                                    .deviceId(config.getDeviceId())
                                    .timestammp(System.currentTimeMillis())
                                    .build()
                    ).then().subscribe();
                    ClientConnection clientConnection=  new ClientConnection(MessageConnection.builder()
                            .connection(connection)
                            .inbound(connection.inbound())
                            .outbound(connection.outbound())
                            .build(),messages) ;
                    connection.inbound().receiveObject().cast(TransportMessage.class)
                            .filter(message->message.getCatagory()==ProtocolCatagory.MSG)
                            .subscribe(transportMessage -> messages.onNext(transportMessage));
                    connection.channel().attr(AttributeKeys.clientConnectionAttributeKey).set(clientConnection);
                    connection.onWriteIdle(config.getHeart()*1000,()->clientConnection.ping().subscribe());
                    connection.onDispose(()->retryConnect(config,clientConnection,messages));
                    return clientConnection;
                }).doOnError(error->log.error("*************************************************************client error {}",error.getMessage()));
    }


    private TcpClient buildClient(Config config){
        TcpClient client= TcpClient.create()
                .port(config.getPort())
                .host(config.getIp())
                .wiretap(config.isLog());
        try {
            SslContext sslClient = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            return  config.isSsl()?client.secure(sslContextSpec -> sslContextSpec.sslContext(sslClient)):client;
        }
        catch (Exception e){
            log.error("*******************************************************************ssl error: {}",e.getMessage());
            return  client;
        }
    }




    private  void   retryConnect(ClientConfig config, ClientConnection clientConnection, UnicastProcessor<TransportMessage>  messages){
        log.info("短线重连中..............................................................");
        buildClient(config)
                .doOnConnected(c -> c.addHandler("decoder",new MessageDecoder2()).addHandler("encoder",new MessageEncoder()))
                .connect()
                .doOnError(error->log.error("*************************************************************client error {}",error.getMessage()))
                .retry()
                .subscribe(connection -> {
                    connection.outbound().sendObject(
                            TransportMessage.builder()
                                    .catagory(ProtocolCatagory.ONLINE)
                                    .deviceId(config.getDeviceId())
                                    .timestammp(System.currentTimeMillis())
                                    .build()
                    ).then().delaySubscription(Duration.ofSeconds(2)).subscribe();
                    clientConnection.setConnection(MessageConnection.builder()
                            .connection(connection)
                            .inbound(connection.inbound())
                            .outbound(connection.outbound())
                            .build());
                    connection.inbound().receiveObject().cast(TransportMessage.class)
                            .filter(message->message.getCatagory()==ProtocolCatagory.MSG)
                            .subscribe(transportMessage -> messages.onNext(transportMessage));
                    connection.channel().attr(AttributeKeys.clientConnectionAttributeKey).set(clientConnection);
                    connection.onReadIdle(config.getHeart()*1000,()->clientConnection.ping().subscribe());
                    connection.onDispose(()->retryConnect(config,clientConnection,messages));
                });
    }


    private TcpServer buildServer(Config config){
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
}
