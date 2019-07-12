package com.iot.common.connection;

import io.netty.util.AttributeKey;
import lombok.experimental.UtilityClass;
import reactor.core.Disposable;

@UtilityClass
public class AttributeKeys {

    public AttributeKey<ClientConnection> clientConnectionAttributeKey = AttributeKey.newInstance("client_operation");

    public AttributeKey<ServerConnection> serverConnectionAttributeKey = AttributeKey.newInstance("server_operation");

    public AttributeKey<Disposable> closeConnection = AttributeKey.newInstance("close_connection");


}
