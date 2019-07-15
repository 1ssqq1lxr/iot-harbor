package com.iot.common.codec;

public enum  ProtocolCatagory {

    ONLINE((byte)0), //在线
    MSG((byte)1),    // 消息
    MSG_BACK((byte)2),  // back
    PING((byte)14),  //心跳
    PONG((byte)15) ;//回复
    private byte number;

    ProtocolCatagory(byte i) {
        this.number=i;
    }
}
