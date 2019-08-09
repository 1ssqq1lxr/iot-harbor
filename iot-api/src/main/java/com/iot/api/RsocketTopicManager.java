package com.iot.api;

import com.iot.common.connection.TransportConnection;
import reactor.core.Disposable;

import java.util.List;

public interface RsocketTopicManager extends Disposable {

     List<TransportConnection>  getConnectionsByTopic(String topic);

     void  addTopicConnection(String topic, TransportConnection connection);





}
