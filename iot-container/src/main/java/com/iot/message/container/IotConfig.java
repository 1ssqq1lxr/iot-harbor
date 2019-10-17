package com.iot.message.container;


import com.iot.common.annocation.ProtocolType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "iot.mqtt")
public class IotConfig {

    private Server server;



     @Data
    public  static  class  Server{
        private  boolean enable;

        private String host;

        private int port;

        private boolean log;

        private ProtocolType protocol;

        private int  heart;

        private boolean ssl;


    }





}
