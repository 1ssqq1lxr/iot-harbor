package com.iot.transport.server.handler.connect;

import com.iot.api.AttributeKeys;
import com.iot.api.MqttMessageApi;
import com.iot.api.RsocketChannelManager;
import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.server.handler.DirectHandler;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.Attribute;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class ConnectHandler implements DirectHandler {


    @Override
    public Mono<Void> handler(MqttMessage message, TransportConnection connection, RsocketServerConfig config) {
        return Mono.fromRunnable(()->{
            switch (message.fixedHeader().messageType()){
                case CONNECT:
                    MqttConnectMessage connectMessage =(MqttConnectMessage) message;
                    MqttConnectVariableHeader connectVariableHeader=connectMessage.variableHeader();
                    MqttConnectPayload mqttConnectPayload=connectMessage.payload();
                    if(connectVariableHeader.hasPassword() && connectVariableHeader.hasUserName()){
                        if(config.getAuth().apply(mqttConnectPayload.userName(),mqttConnectPayload.password()))
                            connectSuccess(connection,config.getChannelManager());
                        else connection.write( MqttMessageApi.buildConnectAck(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD)).subscribe();
                    }
                    else {
                        connectSuccess(connection,config.getChannelManager());
                    }
                    break;
                case DISCONNECT:
                    config.getChannelManager().removeConnections(connection);
                    connection.dispose();
                    break;
            }
        });
    }

    private  void  connectSuccess(TransportConnection connection, RsocketChannelManager channelManager){
        Optional.ofNullable(connection.getConnection().channel().attr(AttributeKeys.closeConnection)) // 取消关闭连接
                .map(Attribute::get)
                .ifPresent(Disposable::dispose);
        channelManager.addConnections(connection);
        connection.write( MqttMessageApi.buildConnectAck(MqttConnectReturnCode.CONNECTION_ACCEPTED)).subscribe();
        connection.write( MqttMessageApi.buildConnectAck(MqttConnectReturnCode.CONNECTION_ACCEPTED)).subscribe();
    }
}
