package com.iot.api;

import java.util.List;

/**
 * manage topic
 */
public interface RsocketTopicManager  {

     List<TransportConnection>  getConnectionsByTopic(String topic);

     void  addTopicConnection(String topic, TransportConnection connection);


     void  deleteTopicConnection(String topic, TransportConnection connection);


}
