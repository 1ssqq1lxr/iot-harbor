package com.iot.api;


import com.iot.common.connection.ReatinMessage;

import java.util.List;

/**
 * manage message
 */
public interface RsocketMessageHandler {

    void saveRetain(boolean dup, boolean retain, int qos, String topicName, byte[] copyByteBuf);

    List<ReatinMessage> getRetain(String topicName);
}
