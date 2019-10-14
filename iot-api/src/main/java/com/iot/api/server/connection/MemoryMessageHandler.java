package com.iot.api.server.connection;

import com.iot.api.RsocketMessageHandler;

public class MemoryMessageHandler implements RsocketMessageHandler {
    @Override
    public void saveRetain(boolean dup, boolean retain, int qos, String topicName, byte[] copyByteBuf) {

    }
}
