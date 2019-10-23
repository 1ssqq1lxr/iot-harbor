package com.iot.api;


import com.iot.common.connection.ReatinMessage;

import java.util.List;
import java.util.Optional;

/**
 * manage message
 */
public interface RsocketMessageHandler {

    void saveRetain(boolean dup, boolean retain, int qos, String topicName, byte[] copyByteBuf);

    Optional<List<ReatinMessage>> getRetain(String topicName);
}
