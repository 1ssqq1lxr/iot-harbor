package com.iot.api;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.*;

import java.util.List;


public class MqttMessageApi {


    public static MqttPublishMessage buildPub(boolean isDup, MqttQoS qoS, boolean isRetain, int messageId, String topic, ByteBuf message){
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH,isDup, qoS,isRetain,0);
        MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader(topic,messageId);
        MqttPublishMessage mqttPublishMessage = new MqttPublishMessage(mqttFixedHeader,mqttPublishVariableHeader, message);
        return  mqttPublishMessage;
    }


    public static MqttPubAckMessage buildPuback(boolean isDup, MqttQoS qoS, boolean isRetain, int messageId){
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK,isDup, qoS,isRetain,2);
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = MqttMessageIdVariableHeader.from(messageId);
        MqttPubAckMessage mqttPubAckMessage = new MqttPubAckMessage(mqttFixedHeader,mqttMessageIdVariableHeader);
        return  mqttPubAckMessage;
    }

    public  static  MqttSubAckMessage buildSubAck(int messageId, List<Integer> qos){
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
        MqttSubAckPayload payload = new MqttSubAckPayload(qos);
        return new MqttSubAckMessage(mqttFixedHeader, variableHeader, payload);
    }


    public static  MqttUnsubAckMessage buildUnsubAck(int messageId){
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0x02);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
        return new MqttUnsubAckMessage(mqttFixedHeader, variableHeader);
    }

    public  static  MqttConnAckMessage buildConnectAck(MqttConnectReturnCode connectReturnCode){
        MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(connectReturnCode,true);
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(
                MqttMessageType.CONNACK,false, MqttQoS.AT_MOST_ONCE, false, 0x02);
        return new MqttConnAckMessage(mqttFixedHeader, mqttConnAckVariableHeader);
    }





}
