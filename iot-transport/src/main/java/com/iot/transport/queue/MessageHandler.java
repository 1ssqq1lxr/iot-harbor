package com.iot.transport.queue;


import com.lmax.disruptor.EventHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;

import static io.netty.handler.codec.mqtt.MqttMessageType.PUBREC;

public class MessageHandler implements EventHandler<MessageEvent> {

    private final MessageTransfer messageTransfer;

    public MessageHandler(MessageTransfer messageTransfer) {
        this.messageTransfer = messageTransfer;
    }


    @Override
    public void onEvent(MessageEvent messageEvent, long l, boolean b) throws Exception {
        SendMqttMessage message=messageEvent.getMessage();
        if(message.getChannel().isActive()){
            switch (message.getConfirmStatus()){
                case PUB:
                    pubMessage(message.getChannel(),message);
                    break;
                case PUBREL:
                    sendAck(MqttMessageType.PUBREL,message);
                    break;
                case PUBREC:
                    sendAck(PUBREC,message);
                    break;
            }
        }
    }

    private   void pubMessage(Channel channel, SendMqttMessage mqttMessage){
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH,true, mqttMessage.getQos(),mqttMessage.isRetain(),0);
        MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader(mqttMessage.getTopic(),mqttMessage.getMessageId());
        MqttPublishMessage mqttPublishMessage = new MqttPublishMessage(mqttFixedHeader,mqttPublishVariableHeader, Unpooled.wrappedBuffer(mqttMessage.getByteBuf()));
        channel.writeAndFlush(mqttPublishMessage);
    }

    protected void  sendAck(MqttMessageType type, SendMqttMessage mqttMessage){
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(type,true, MqttQoS.AT_LEAST_ONCE,false,0x02);
        MqttMessageIdVariableHeader from = MqttMessageIdVariableHeader.from(mqttMessage.getMessageId());
        MqttPubAckMessage mqttPubAckMessage = new MqttPubAckMessage(mqttFixedHeader,from);
        mqttMessage.getChannel().writeAndFlush(mqttPubAckMessage);
    }

}
