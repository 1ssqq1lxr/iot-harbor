package com.iot.config;

import com.iot.api.RsocketConfiguration;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


@Getter
@Setter
@ToString
public class RsocketClientConfig implements RsocketConfiguration {

    private String ip;

    private int  port;

    private String  protocol;

    private int heart = 30;

    private boolean log;

    private boolean ssl;

    private Options options = new Options();


    private Consumer<Throwable> throwableConsumer;

    private BiConsumer<String,byte[]> messageAcceptor;

    private Runnable onClose = ()->{};


    public void checkConfig() {
        Objects.requireNonNull(ip,"ip is not null");
        Objects.requireNonNull(port,"port is not null");
        Objects.requireNonNull(protocol,"protocol is not null");
        Objects.requireNonNull(options.getClientIdentifier(),"clientIdentifier is not null");
        if(options.isHasWillFlag()){
            Objects.requireNonNull(options.getWillMessage(),"willMessage is not null");
            Objects.requireNonNull(options.getWillQos(),"willQos is not null");
            Objects.requireNonNull(options.getWillTopic(),"willTopic is not null");
        }
    }


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


    }

}
