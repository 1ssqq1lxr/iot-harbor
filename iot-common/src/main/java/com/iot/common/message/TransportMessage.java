package com.iot.common.message;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransportMessage  {

   private String topic;

   private String message;

   private int   qos;


}
