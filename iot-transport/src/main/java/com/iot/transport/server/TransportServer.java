package com.iot.transport.server;


import com.iot.api.RsocketMessageHandler;
import com.iot.api.server.RsocketServerSession;
import com.iot.common.annocation.ProtocolType;
import com.iot.config.RsocketServerConfig;
import reactor.core.publisher.Mono;
import reactor.netty.ConnectionObserver;

import java.util.Optional;
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
    
        public TransportServer.TransportBuilder keepAlive(boolean  isKeepAlive){
            config.setKeepAlive(isKeepAlive);
            return this;
        }
        public TransportServer.TransportBuilder noDelay(boolean  noDelay){
            config.setNoDelay(noDelay);
            return this;
        }
    
        public TransportServer.TransportBuilder backlog(int  length){
            config.setBacklog(length);
            return this;
        }
        public TransportServer.TransportBuilder sendBufSize(int  size){
            config.setSendBufSize(size);
            return this;
        }
    
        public TransportServer.TransportBuilder revBufSize(int  size){
            config.setRevBufSize(size);
            return this;
        }

        
        public TransportServer.TransportBuilder auth(BiFunction<String,String,Boolean> auth){
            config.setAuth(auth);
            return this;
        }


        public TransportServer.TransportBuilder messageHandler(RsocketMessageHandler messageHandler ){
            Optional.ofNullable(messageHandler)
                    .ifPresent(config::setMessageHandler);
            return this;
        }
        
        public TransportServer.TransportBuilder exception(Consumer<Throwable> exceptionConsumer ){
            Optional.ofNullable(exceptionConsumer)
                    .ifPresent(config::setThrowableConsumer);
            return this;
        }
        

        public Mono<RsocketServerSession> start(){
            config.checkConfig();
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
