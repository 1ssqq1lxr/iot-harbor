package com.iot.transport.server.handler.connect;

import com.iot.api.AttributeKeys;
import com.iot.api.MqttMessageApi;
import com.iot.api.RsocketChannelManager;
import com.iot.api.RsocketConfiguration;
import com.iot.api.TransportConnection;
import com.iot.common.connection.WillMessage;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.DirectHandler;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.Attribute;
import reactor.core.Disposable;

import java.util.Optional;

public class ConnectHandler implements DirectHandler {


    @Override
    public void handler(MqttMessage message, TransportConnection connection, RsocketConfiguration config) {
            RsocketServerConfig serverConfig = (RsocketServerConfig) config;
            switch (message.fixedHeader().messageType()){
                case CONNECT:
                    MqttConnectMessage connectMessage =(MqttConnectMessage) message;
                    MqttConnectVariableHeader connectVariableHeader=connectMessage.variableHeader();
                    MqttConnectPayload mqttConnectPayload=connectMessage.payload();
                    RsocketChannelManager channelManager=serverConfig.getChannelManager();
                    String clientId=mqttConnectPayload.clientIdentifier();
                    if(channelManager.checkDeviceId(clientId)){
                        connection.write( MqttMessageApi.buildConnectAck(MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED)).subscribe();
                        connection.dispose();
                    }
                    else {
                        if(connectVariableHeader.hasPassword() && connectVariableHeader.hasUserName()){
                            if(serverConfig.getAuth().apply(mqttConnectPayload.userName(),mqttConnectPayload.password()))
                                connectSuccess(connection,serverConfig.getChannelManager(),clientId,connectVariableHeader.keepAliveTimeSeconds());
                            else connection.write( MqttMessageApi.buildConnectAck(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD)).subscribe();
                            if(connectVariableHeader.isWillFlag())
                                saveWill(connection,mqttConnectPayload.willTopic(),connectVariableHeader.isWillRetain(),mqttConnectPayload.willMessageInBytes(),connectVariableHeader.willQos());
                        }
                        else {
                            connectSuccess(connection,channelManager,mqttConnectPayload.clientIdentifier(),connectVariableHeader.keepAliveTimeSeconds());
                            if(connectVariableHeader.isWillFlag())
                                saveWill(connection,mqttConnectPayload.willTopic(),connectVariableHeader.isWillRetain(),mqttConnectPayload.willMessageInBytes(),connectVariableHeader.willQos());
                        }
                        break;
                    }
                case DISCONNECT:
                    serverConfig.getChannelManager().removeConnections(connection);
                    connection.dispose();
                    break;
            }
    }

    private void   saveWill(TransportConnection connection,String willTopic,boolean willRetain ,byte[] willMessage,int qoS){
        WillMessage ws =  new WillMessage(qoS,willTopic,willMessage,willRetain);
        connection.getConnection().channel().attr(AttributeKeys.WILL_MESSAGE).set(ws); // 设置device Id
    }

    private  void  connectSuccess(TransportConnection connection, RsocketChannelManager channelManager, String deviceId, int keepalived){
        connection.getConnection().onReadIdle(keepalived*2000, () -> connection.getConnection().dispose()); // 心跳超时关闭
        connection.getConnection().channel().attr(AttributeKeys.keepalived).set(keepalived); // 设置device Id
        channelManager.addDeviceId(deviceId,connection);
        Optional.ofNullable(connection.getConnection().channel().attr(AttributeKeys.closeConnection)) // 取消关闭连接
                .map(Attribute::get)
                .ifPresent(Disposable::dispose);
        channelManager.addConnections(connection);
        connection.write( MqttMessageApi.buildConnectAck(MqttConnectReturnCode.CONNECTION_ACCEPTED)).subscribe();

    }
}
