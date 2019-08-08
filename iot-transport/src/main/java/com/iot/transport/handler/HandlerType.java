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



    CONNECT(),
    PUB(),
    SUB(),
    HEART();





}
