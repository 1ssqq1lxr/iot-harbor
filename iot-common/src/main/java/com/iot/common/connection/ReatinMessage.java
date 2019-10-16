package com.iot.common.connection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReatinMessage {
    private boolean dup;
    private boolean retain;
    private int qos;
    private String topicName;
    private byte[] copyByteBuf;
}
