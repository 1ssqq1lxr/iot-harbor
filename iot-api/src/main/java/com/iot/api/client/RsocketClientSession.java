package com.iot.api.client;

import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RsocketClientSession  extends  Disposable{


    Mono<Void> pub(String topic, String message, boolean retained, int qos);

    Mono<Void> pub(String topic,String message);

    Mono<Void> pub(String topic,String message,int qos);

    Mono<Void> pub(String topic,String message,boolean retained);

    Mono<Void> sub(String... subMessages);

    Mono<Void> unsub(List<String> topics);

    Mono<Void> unsub();
}
