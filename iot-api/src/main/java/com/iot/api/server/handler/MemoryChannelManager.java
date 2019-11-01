package com.iot.api.server.handler;

import com.iot.api.RsocketChannelManager;
import com.iot.api.TransportConnection;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class MemoryChannelManager implements RsocketChannelManager {


    private CopyOnWriteArrayList<TransportConnection> connections = new CopyOnWriteArrayList<>();

    private ConcurrentHashMap<String,TransportConnection> connectionMap = new ConcurrentHashMap<>();

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

    @Override
    public void addDeviceId(String deviceId, TransportConnection connection) {
        connectionMap.put(deviceId,connection);
    }

    @Override
    public void removeDeviceId(String deviceId) {
        connectionMap.remove(deviceId);
    }

    @Override
    public TransportConnection getRemoveDeviceId(String deviceId) {
        return connectionMap.remove(deviceId);
    }


}
