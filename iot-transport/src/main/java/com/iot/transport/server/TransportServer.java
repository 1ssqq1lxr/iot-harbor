package com.iot.transport.server;


import com.iot.api.RsocketMessageHandler;
import com.iot.api.server.RsocketServerSession;
import com.iot.common.annocation.ProtocolType;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.server.connection.RsocketServerConnection;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class TransportServer  {
    private static RsocketServerConfig config;

    private static TransportServerFactory transportFactory;


    private  TransportServer(){

    }
    public  static class TransportBuilder{

        public TransportBuilder(){
            config = new RsocketServerConfig();
            transportFactory = new TransportServerFactory();
        }

        public TransportBuilder(String ip,int port){
            this();
            config.setIp(ip);
            config.setPort(port);
        }

        public TransportServer.TransportBuilder protocol(ProtocolType protocolType){
            config.setProtocol(protocolType.name());
            return this;
        }

        public TransportServer.TransportBuilder heart(int  heart){
            config.setHeart(heart);
            return this;
        }

        public TransportServer.TransportBuilder ssl(boolean  ssl){
            config.setSsl(ssl);
            return this;
        }

        public TransportServer.TransportBuilder log(boolean  log){
            config.setLog(log);
            return this;
        }


        public TransportServer.TransportBuilder auth(BiFunction<String,String,Boolean> auth){
            config.setAuth(auth);
            return this;
        }


        public TransportServer.TransportBuilder messageHandler(RsocketMessageHandler messageHandler ){
            config.setMessageHandler(messageHandler);
            return this;
        }

        public TransportServer.TransportBuilder exception(Consumer<Throwable> exceptionConsumer ){
            config.setExceptionConsumer(exceptionConsumer);
            return this;
        }

        public Mono<RsocketServerSession> start(){
            return transportFactory.start(config);
        }
    }

    public static TransportServer.TransportBuilder create(String ip, int port){
        return  new TransportServer.TransportBuilder(ip,port);
    }

    public static  TransportServer.TransportBuilder create(){
        return  new TransportServer.TransportBuilder();
    }
}
