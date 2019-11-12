package com.mqtt.test;

import com.iot.container.AuthencationSession;
import org.springframework.stereotype.Component;

public class MqttAuthencation implements AuthencationSession {
    @Override
    public boolean auth(String username, String password) {
        return true;
    }
}
