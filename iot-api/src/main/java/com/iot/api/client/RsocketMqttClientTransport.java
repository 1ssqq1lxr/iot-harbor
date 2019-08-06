package com.iot.api.client;

import com.iot.common.message.TransportMessage;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

public interface RsocketMqttClientTransport extends Disposable {

    Mono<Void>   connect(TransportMessage transportMessage);

    Mono<Void>   publish(TransportMessage message);

}
