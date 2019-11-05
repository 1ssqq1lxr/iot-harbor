package com.iot.protocol.mqtt;

import com.google.common.collect.Lists;
import com.iot.protocol.Protocol;
import com.iot.common.annocation.ProtocolType;
import com.iot.protocol.ProtocolTransport;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;

import java.util.List;


public class MqttProtocol implements Protocol {


    @Override
    public boolean support(ProtocolType protocolType) {
        return protocolType == ProtocolType.MQTT;
    }

    @Override
    public ProtocolTransport getTransport() {
        return  new MqttTransport(this);
    }

    @Override
    public List<ChannelHandler> getHandlers() {
        return Lists.newArrayList( new MqttDecoder(5*1024*1024),MqttEncoder.INSTANCE);
    }
}
