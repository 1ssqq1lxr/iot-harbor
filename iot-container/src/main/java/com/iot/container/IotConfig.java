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
    
        /**
         * 发送缓冲区大小
         * 默认 32k
         */
        private int sendBufSize = 32 * 1024;
        /**
         * 接收缓冲区大小
         * 默认 32k
         */
        private int revBufSize = 32 * 1024;
    
        /**
         *Socket参数，服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝。默认值
         * 这里默认设置 128
         */
        private int backlog = 128;
        
        /**
         *  Socket参数，连接保活，默认值为False。启用该功能时，TCP会主动探测空闲连接的有效性。可以将此功能视为TCP的心跳机制，
         *  需要注意的是：默认的心跳间隔是7200s即2小时。Netty默认关闭该功能。
         */
        private boolean keepAlive = false;
        /**
         *  Socket参数，立即发送数据，默认值为True（Netty默认为True而操作系统默认为False）。该值设置Nagle算法的启用，
         *  该算法将小的碎片数据连接成更大的报文来最小化所发送的报文的数量，
         *  如果需要发送一些较小的报文，则需要禁用该算法。
         *  Netty默认禁用该算法，从而最小化报文传输延时
         */
        private boolean noDelay = true;



    }


    @Data
    public  static  class  Client{
        private  boolean enable;

        private String ip;

        private int  port;

        private ProtocolType  protocol;

        private int heart ;

        private boolean log;

        private boolean ssl;
    
    
        /**
         * 发送缓冲区大小
         * 默认 32k
         */
        private int sendBufSize = 32 * 1024;
        /**
         * 接收缓冲区大小
         * 默认 32k
         */
        private int revBufSize = 32 * 1024;
    
        /**
         *Socket参数，服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝。默认值
         * 这里默认设置 128
         */
        private int backlog = 128;
    
        /**
         *  Socket参数，连接保活，默认值为False。启用该功能时，TCP会主动探测空闲连接的有效性。可以将此功能视为TCP的心跳机制，
         *  需要注意的是：默认的心跳间隔是7200s即2小时。Netty默认关闭该功能。
         */
        private boolean keepAlive = false;
        /**
         *  Socket参数，立即发送数据，默认值为True（Netty默认为True而操作系统默认为False）。该值设置Nagle算法的启用，
         *  该算法将小的碎片数据连接成更大的报文来最小化所发送的报文的数量，
         *  如果需要发送一些较小的报文，则需要禁用该算法。
         *  Netty默认禁用该算法，从而最小化报文传输延时
         */
        private boolean noDelay = true;


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
