package com.iot.api;

import com.iot.common.connection.TransportConnection;
import reactor.core.Disposable;

import java.util.List;

/**
 * manage topic
 */
public interface RsocketTopicManager extends Disposable {

     List<TransportConnection>  getConnectionsByTopic(String topic);

     void  addTopicConnection(String topic, TransportConnection connection);


     void  deleteTopicConnection(String topic, TransportConnection connection);


}
