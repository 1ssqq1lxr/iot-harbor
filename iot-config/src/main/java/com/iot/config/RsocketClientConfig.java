package com.iot.config;

import com.iot.api.RsocketConfiguration;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.function.BiConsumer;
import java.util.function.Consumer;


@Getter
@Setter
@ToString
public class RsocketClientConfig implements RsocketConfiguration {

    private String ip;

    private int  port;

    private String  protocol;

    private int heart;

    private boolean log;

    private boolean ssl;

    private Options options;


    private Consumer<Throwable> throwableConsumer;

    private BiConsumer<String,byte[]> messageAcceptor;

    private Runnable onClose;

    private Runnable onConnected;



    @Getter
    @Setter
    @ToString
    public class Options{

        private  String clientIdentifier;

        private  String willTopic;

        private  String willMessage;

        private  String userName;

        private  String password;

        private  boolean hasUserName;

        private  boolean hasPassword;

        private  boolean hasWillRetain;

        private  int willQos;

        private  boolean hasWillFlag;

        private  boolean hasCleanSession;

        private int heart;

        private boolean log;
    }

}
