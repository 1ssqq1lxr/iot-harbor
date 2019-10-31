package com.iot.config;

import com.iot.api.RsocketChannelManager;
import com.iot.api.RsocketConfiguration;
import com.iot.api.RsocketMessageHandler;
import com.iot.api.RsocketTopicManager;
import com.iot.api.server.handler.MemoryChannelManager;
import com.iot.api.server.handler.MemoryMessageHandler;
import com.iot.api.server.handler.MemoryTopicManager;
import io.netty.util.Constant;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Getter
@Setter
public class RsocketServerConfig implements RsocketConfiguration {

    private String ip;

    private int  port;

    private String protocol;

    private int heart = 60;

    private boolean log ;

    private boolean ssl ;

    private BiFunction<String,String,Boolean> auth = (user,pass)->true;

    private Consumer<Throwable> throwableConsumer = throwable -> {};


    private RsocketMessageHandler messageHandler = new MemoryMessageHandler();

    private RsocketChannelManager channelManager = new MemoryChannelManager();

    private RsocketTopicManager topicManager = new MemoryTopicManager();


    @Override
    public void checkConfig() {
        Objects.requireNonNull(ip,"ip is not null");
        Objects.requireNonNull(protocol,"protocol is not null");
    }
}
