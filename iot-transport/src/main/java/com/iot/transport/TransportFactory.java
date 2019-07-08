package com.iot.transport;

import com.iot.common.transport.Transport;

public interface TransportFactory {
    Transport  newInstance();
}
