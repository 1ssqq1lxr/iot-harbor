package com.iot.api.server.connection;


import com.iot.api.RsocketTopicManager;
import com.iot.common.connection.TransportConnection;

import java.util.List;

public class MemoryTopicManager implements RsocketTopicManager {


    @Override
    public List<TransportConnection> getConnectionsByTopic(String topic) {
        return null;
    }

    @Override
    public void addTopicConnection(String topic, TransportConnection connection) {

    }

    @Override
    public void dispose() {

    }
}
