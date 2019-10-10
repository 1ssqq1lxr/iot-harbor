package com.iot.api;


/**
 * manage message
 */
public interface RsocketMessageHandler {

    void saveRetain(boolean dup, boolean retain, int qos, String topicName, byte[] copyByteBuf);
}
