package com.iot.transport.server;


import com.iot.api.RsocketClientAbsOperation;
import com.iot.api.RsocketServerAbsOperation;
import com.iot.common.annocation.ProtocolType;
import com.iot.config.RsocketServerConfig;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

public class TransportServer  {
    private RsocketServerConfig config;

    private TransportServerFactory transportFactory;


    private  TransportServer(){

    }
    private class TransportBuilder{

        public TransportBuilder(){
            config = new RsocketServerConfig();
            transportFactory = new TransportServerFactory();
        }

        public TransportBuilder(String ip,int port){
            super();
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

        public TransportServer.TransportBuilder auth(BiFunction<String,String,Boolean> auth){
            config.setAuth(auth);
            return this;
        }


        public Mono<RsocketServerAbsOperation> start(){
            return transportFactory.connect(config);
        }
    }

    public TransportServer.TransportBuilder create(String ip, int port){
        return  new TransportServer.TransportBuilder(ip,port);
    }

    public TransportServer.TransportBuilder create(){
        return  new TransportServer.TransportBuilder();
    }
}
