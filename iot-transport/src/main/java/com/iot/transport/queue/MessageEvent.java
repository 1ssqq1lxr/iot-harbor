package com.iot.transport.queue;

import com.iot.common.message.TransportMessage;
import lombok.Data;

@Data
public class MessageEvent {

    private TransportMessage message;


}
