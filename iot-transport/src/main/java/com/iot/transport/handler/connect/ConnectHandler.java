package com.iot.transport.handler.connect;

import com.iot.api.AttributeKeys;
import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.handler.DirectHandler;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.util.Attribute;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class ConnectHandler implements DirectHandler {


    @Override
    public Mono<Void> handler(MqttMessage message, TransportConnection connection, RsocketServerConfig config) {
        return Mono.fromRunnable(()->{
            MqttConnectMessage connectMessage =(MqttConnectMessage) message;
            MqttConnectVariableHeader connectVariableHeader=connectMessage.variableHeader();
            MqttConnectPayload mqttConnectPayload=connectMessage.payload();
            if(connectVariableHeader.hasPassword() && connectVariableHeader.hasUserName() && config.getAuth().apply(mqttConnectPayload.userName(),mqttConnectPayload.password())){
                Optional.ofNullable(connection.getConnection().channel().attr(AttributeKeys.closeConnection)) // 取消关闭连接
                        .map(Attribute::get)
                        .ifPresent(Disposable::dispose);
                config.getChannelManager().addConnections(connection);
            }
        });
    }
}
