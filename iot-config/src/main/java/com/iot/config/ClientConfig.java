package com.iot.config;

import com.iot.common.connection.ClientConnection;
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

    private Consumer<ClientConnection> connectionConsumer;

}
