package com.iot.api;

import com.iot.common.connection.TransportConnection;
import io.netty.util.AttributeKey;
import lombok.experimental.UtilityClass;
import reactor.core.Disposable;

@UtilityClass
public class AttributeKeys {

    public AttributeKey<RsocketClientAbsOperation> clientConnectionAttributeKey = AttributeKey.valueOf("client_operation");

    public AttributeKey<RsocketServerAbsOperation> serverConnectionAttributeKey = AttributeKey.valueOf("server_operation");

    public AttributeKey<Disposable> closeConnection = AttributeKey.valueOf("close_connection");

    public AttributeKey<TransportConnection> connectionAttributeKey = AttributeKey.valueOf("transport_connection");

}
