package com.iot.transport;


import com.iot.common.connection.ClientConnection;
import com.iot.config.ClientConfig;
import com.iot.protocol.ProtocolType;
import reactor.core.publisher.Mono;

public class TransportClient {

    private ClientConfig config;

    private  TransportClient(){

    }
    private class TransportBuilder{

        public TransportBuilder(){
            config = new ClientConfig();
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

        public Mono<ClientConnection> connect(){
            return Mono.empty();
        }

    }

    public TransportBuilder create(String ip,int port){
        return  new TransportBuilder(ip,port);
    }

    public TransportBuilder create(){
        return  new TransportBuilder();
    }





}
