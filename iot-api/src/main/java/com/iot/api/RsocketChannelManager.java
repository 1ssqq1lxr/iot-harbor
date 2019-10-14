package com.iot.api;

import com.iot.common.connection.TransportConnection;
import reactor.core.Disposable;

import java.util.List;

public interface RsocketChannelManager  {

     List<TransportConnection> getConnections();


     void  addConnections(TransportConnection connection);


     void removeConnections(TransportConnection connection);
}
