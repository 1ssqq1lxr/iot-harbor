package com.iot.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientConfig implements Config {

    private String ip;

    private int  port;

    private String  protocol;

}
