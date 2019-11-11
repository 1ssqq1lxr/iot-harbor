package com.iot.container;

public interface MessageAcceptor {

    void accept(String topic,byte[] message);

}
