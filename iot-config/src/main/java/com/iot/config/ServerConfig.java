package com.iot.config;

import com.iot.api.Config;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerConfig implements Config {

    private String ip;

    private int  port;

    private String protocol;

    private int heart;

    private boolean log;

    private boolean ssl;



}
