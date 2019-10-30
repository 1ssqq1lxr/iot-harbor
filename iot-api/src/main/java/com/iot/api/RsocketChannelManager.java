package com.iot.api;

import java.util.List;

public interface RsocketChannelManager  {

     List<TransportConnection> getConnections();


     void  addConnections(TransportConnection connection);


     void removeConnections(TransportConnection connection);
}
