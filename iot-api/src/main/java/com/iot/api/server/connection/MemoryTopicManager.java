package com.iot.api.server.connection;


import com.google.common.collect.Lists;
import com.iot.api.RsocketTopicManager;
import com.iot.common.connection.TransportConnection;

import java.util.List;

public class MemoryTopicManager implements RsocketTopicManager {


    @Override
    public List<TransportConnection> getConnectionsByTopic(String topic) {
        return Lists.newArrayList();
    }

    @Override
    public void addTopicConnection(String topic, TransportConnection connection) {

    }

    @Override
    public void deleteTopicConnection(String topic, TransportConnection connection) {

    }

    @Override
    public void dispose() {

    }
}
