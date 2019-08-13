package com.iot.transport.connection;

import com.iot.api.RsocketChannelManager;
import com.iot.common.connection.TransportConnection;

import java.util.List;

public class MemoryChannelManager implements RsocketChannelManager {


    @Override
    public List<TransportConnection> getConnections() {
        return null;
    }

    @Override
    public TransportConnection getConnection(String clientId) {
        return null;
    }


    @Override
    public void addConnection(String clientId, TransportConnection connection) {

    }

    @Override
    public void dispose() {

    }




}
