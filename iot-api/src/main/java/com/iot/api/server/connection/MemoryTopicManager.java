package com.iot.api.server.connection;


import com.google.common.collect.Lists;
import com.iot.api.RsocketTopicManager;
import com.iot.api.server.path.TopicManager;
import com.iot.common.connection.TransportConnection;

import java.util.List;

public class MemoryTopicManager implements RsocketTopicManager {


    private TopicManager topicManager = new TopicManager();

    @Override
    public List<TransportConnection> getConnectionsByTopic(String topic) {
        return topicManager.getTopicConnection(topic).orElse(Lists.newArrayList());
    }

    @Override
    public void addTopicConnection(String topic, TransportConnection connection) {
        topicManager.addTopicConnection(topic,connection);
    }

    @Override
    public void deleteTopicConnection(String topic, TransportConnection connection) {
        topicManager.deleteTopicConnection(topic,connection);
    }

}
