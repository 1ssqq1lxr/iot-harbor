package com.iot.config;

import com.iot.common.connection.ClientOperation;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;


@Getter
@Setter
public class ClientConfig implements Config {

    private String ip;

    private int  port;

    private String  protocol;

    private int heart;

    private boolean isLog;

    private Consumer<ClientOperation> connectionConsumer;

}
