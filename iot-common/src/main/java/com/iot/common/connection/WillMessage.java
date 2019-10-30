package com.iot.common.connection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WillMessage {
    private int qos;
    private String topicName;
    private byte[] copyByteBuf;
}
