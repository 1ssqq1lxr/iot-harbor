package com.iot.config;

import com.iot.api.RsocketChannelManager;
import com.iot.api.RsocketConfiguration;
import com.iot.api.RsocketMessageHandler;
import com.iot.api.RsocketTopicManager;
import com.iot.api.server.connection.MemoryChannelManager;
import com.iot.api.server.connection.MemoryMessageHandler;
import com.iot.api.server.connection.MemoryTopicManager;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BiFunction;

@Getter
@Setter
public class RsocketServerConfig implements RsocketConfiguration {

    private String ip;

    private int  port;

    private String protocol;

    private int heart;

    private boolean log;

    private boolean ssl;

    private BiFunction<String,String,Boolean> auth;


    private RsocketMessageHandler messageHandler = new MemoryMessageHandler();

    private RsocketChannelManager channelManager = new MemoryChannelManager();

    private RsocketTopicManager topicManager = new MemoryTopicManager();



}
