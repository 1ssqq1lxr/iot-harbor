package com.iot.api.client;

@FunctionalInterface
public interface ClientMessageConsumer {
    void  recive(String topic,  byte[] message) ;
}
