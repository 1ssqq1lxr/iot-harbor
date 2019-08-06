package com.iot.transport;


import com.iot.api.RsocketClientAbsOperation;
import com.iot.config.ClientConfig;
import com.iot.common.annocation.ProtocolType;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public class TransportClient {

    private ClientConfig config;

    private TransportClientFactory transportFactory;

    private  TransportClient(){

    }
    private class TransportBuilder{

        public TransportBuilder(){
            config = new ClientConfig();
            transportFactory = new TransportClientFactory();
        }

        public TransportBuilder(String ip,int port){
            config = new ClientConfig();
            config.setIp(ip);
            config.setPort(port);
        }

        public TransportBuilder protocol(ProtocolType protocolType){
             config.setProtocol(protocolType.name());
             return this;
        }

        public TransportBuilder heart(int  heart){
            config.setHeart(heart);
            return this;
        }


        public Mono<RsocketClientAbsOperation> connect(){
            return transportFactory.connect(config);
        }

    }

    public TransportBuilder create(String ip,int port){
        return  new TransportBuilder(ip,port);
    }

    public TransportBuilder create(){
        return  new TransportBuilder();
    }





}
