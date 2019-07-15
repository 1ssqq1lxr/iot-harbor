package com.iot.common.message;

import com.iot.common.codec.ProtocolCatagory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransportMessage  {

    private Object message;

    private String deviceId;

    private int messageId;

    private long timestammp;

    private String topic;

    private ProtocolCatagory catagory;


}
