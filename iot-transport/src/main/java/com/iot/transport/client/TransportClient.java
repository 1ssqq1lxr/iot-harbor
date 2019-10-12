//package com.iot.transport.client;
//
//
//import com.iot.api.RsocketClientAbsOperation;
//import com.iot.config.RsocketClientConfig;
//import com.iot.common.annocation.ProtocolType;
//import io.netty.handler.codec.mqtt.MqttQoS;
//import reactor.core.publisher.Mono;
//
//public class TransportClient {
//
//    private RsocketClientConfig config;
//
//    private TransportClientFactory transportFactory;
//
//    private RsocketClientConfig.Options options;
//
//    private  TransportClient(){
//
//    }
//    private class TransportBuilder{
//
//        public TransportBuilder(){
//            config = new RsocketClientConfig();
//            transportFactory = new TransportClientFactory();
//            options = config.new Options();
//            config.setOptions(options);
//        }
//
//        public TransportBuilder(String ip,int port){
//            config = new RsocketClientConfig();
//            config.setIp(ip);
//            config.setPort(port);
//        }
//
//        public TransportBuilder protocol(ProtocolType protocolType){
//             config.setProtocol(protocolType.name());
//             return this;
//        }
//
//        public TransportBuilder heart(int  heart){
//            config.setHeart(heart);
//            return this;
//        }
//
//        public TransportBuilder ssl(boolean  ssl){
//            config.setSsl(ssl);
//            return this;
//        }
//
//
//        public TransportBuilder clientId(String   clientId){
//            options.setClientIdentifier(clientId);
//            return this;
//        }
//
//        public  TransportBuilder  username(String username){
//            options.setUserName(username);
//            return this;
//        }
//
//
//        public  TransportBuilder  password(String password){
//            options.setPassword(password);
//            return this;
//        }
//
//        public  TransportBuilder  willTopic(String willTopic){
//            options.setWillTopic(willTopic);
//            return this;
//        }
//
//        public  TransportBuilder  willMessage(String willMessage){
//            options.setWillMessage(willMessage);
//            return this;
//        }
//
//        public  TransportBuilder  willQos(MqttQoS qoS){
//            options.setWillQos(qoS.value());
//            return this;
//        }
//
//        public Mono<RsocketClientAbsOperation> connect(){
//            return transportFactory.connect(config);
//        }
//
//    }
//
//    public TransportBuilder create(String ip,int port){
//        return  new TransportBuilder(ip,port);
//    }
//
//    public TransportBuilder create(){
//        return  new TransportBuilder();
//    }
//
//
//
//
//
//}
