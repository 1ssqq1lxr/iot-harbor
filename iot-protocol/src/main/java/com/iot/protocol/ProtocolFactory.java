package com.iot.protocol;

import java.util.List;
import java.util.Optional;

public class ProtocolFactory {

    private final List<Protocol> protocols;

    public ProtocolFactory(List<Protocol> protocols) {
        this.protocols = protocols;
    }

    public void  registryProtocl(Protocol protocol){
        protocols.add(protocol);
    }

    public Optional<Protocol>  getProtocol(ProtocolType protocolType){
        return protocols.stream().filter(protocol -> protocol.support(protocolType)).findAny();
    }



}
