package com.iot.protocol;

import com.iot.protocol.mqtt.MqttProtocol;
import com.iot.protocol.tcp.TcpProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProtocolFactory {

    private  List<Protocol> protocols = new ArrayList<>();

    public ProtocolFactory(){
        protocols.add(new MqttProtocol());
        protocols.add(new TcpProtocol());
    }


    public void  registryProtocl(Protocol protocol){
        protocols.add(protocol);
    }

    public Optional<Protocol>  getProtocol(ProtocolType protocolType){
        return protocols.stream().filter(protocol -> protocol.support(protocolType)).findAny();
    }



}
