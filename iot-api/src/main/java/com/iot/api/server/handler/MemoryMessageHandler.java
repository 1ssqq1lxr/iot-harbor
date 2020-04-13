package com.iot.api.server.handler;

import com.iot.api.RsocketMessageHandler;
import com.iot.common.connection.RetainMessage;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryMessageHandler implements RsocketMessageHandler {

    private ConcurrentHashMap<String ,RetainMessage> messages = new ConcurrentHashMap();

    @Override
    public void saveRetain(boolean dup, boolean retain, int qos, String topicName, byte[] copyByteBuf) {
        messages.put(topicName,new RetainMessage(dup,retain,qos,topicName,copyByteBuf));
    }

    @Override
    public Optional<RetainMessage> getRetain(String topicName) {
        return Optional.ofNullable(messages.get(topicName));
    }
}
