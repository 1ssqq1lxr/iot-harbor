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
    public void addConnections(TransportConnection connection) {

    }


    @Override
    public void dispose() {

    }




}
