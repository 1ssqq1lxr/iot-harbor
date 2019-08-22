package com.iot.transport.handler;


import com.iot.transport.handler.connect.ConnectHandler;
import com.iot.transport.handler.heart.HeartHandler;
import com.iot.transport.handler.pub.PubHandler;
import com.iot.transport.handler.sub.SubHandler;
import lombok.Getter;

/**
 * 客户端类型
 */
@Getter
public enum HandlerType {



    CONNECT(new ConnectHandler()),
    PUB(new PubHandler()),
    SUB(new SubHandler()),
    HEART(new HeartHandler());

    private  DirectHandler directHandler;

    HandlerType(DirectHandler directHandler) {
        this.directHandler = directHandler;
    }

    public DirectHandler getDirectHandler() {
        return directHandler;
    }
}
