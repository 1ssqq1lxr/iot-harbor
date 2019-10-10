package com.iot.api;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.*;


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





}
