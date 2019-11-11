package com.iot.container;


import com.iot.common.annocation.ProtocolType;
import com.iot.config.RsocketClientConfig;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Data
@Configuration
@ConfigurationProperties(prefix = "iot.mqtt")
public class IotConfig {

    private Server server;

    private Client client;



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


    @Data
    public  static  class  Client{
        private String ip;

        private int  port;

        private ProtocolType  protocol;

        private int heart ;

        private boolean log;

        private boolean ssl;


        private Consumer<Throwable> throwableConsumer;

        private BiConsumer<String,byte[]> messageAcceptor;

        private Runnable onClose = ()->{};

        private Options option;

        @Data
        public  static  class  Options{

            private  String clientIdentifier;

            private  String willTopic;

            private  String willMessage;

            private  String userName;

            private  String password;

            private  boolean hasUserName;

            private  boolean hasPassword;

            private  boolean hasWillRetain;

            private MqttQoS willQos;

            private  boolean hasWillFlag;

            private  boolean hasCleanSession;

        }


    }




}
