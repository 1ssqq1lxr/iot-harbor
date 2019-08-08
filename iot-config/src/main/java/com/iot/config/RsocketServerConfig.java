package com.iot.config;

import com.iot.api.RsocketConfiguration;
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



}
