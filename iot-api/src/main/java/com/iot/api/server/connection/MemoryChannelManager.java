package com.iot.api.server.connection;

import com.iot.api.RsocketChannelManager;
import com.iot.common.connection.TransportConnection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MemoryChannelManager implements RsocketChannelManager {


    private CopyOnWriteArrayList<TransportConnection> connections = new CopyOnWriteArrayList<>();

    @Override
    public List<TransportConnection> getConnections() {
        return connections;
    }

    @Override
    public void addConnections(TransportConnection connection) {
        connections.add(connection);
    }

    @Override
    public void removeConnections(TransportConnection connection) {
        connections.remove(connection);
    }





}
