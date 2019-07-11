package com.iot.common;

public enum  Qos {
    AtMostOnce((byte)0),
    AtLeastOnce((byte)1);
    private byte i;
    Qos(byte i) {
        this.i = i;
    }
}
