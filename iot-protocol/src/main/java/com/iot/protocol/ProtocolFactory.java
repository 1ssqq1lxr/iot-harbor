package com.iot.protocol;

import com.iot.common.annocation.ProtocolType;
import com.iot.api.Protocol;
import com.iot.protocol.mqtt.MqttProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProtocolFactory {

    private  List<Protocol> protocols = new ArrayList<>();

    public ProtocolFactory(){
        protocols.add(new MqttProtocol());
    }


    public void  registryProtocl(Protocol protocol){
        protocols.add(protocol);
    }

    public Optional<Protocol>  getProtocol(ProtocolType protocolType){
        return protocols.stream().filter(protocol -> protocol.support(protocolType)).findAny();
    }



}
