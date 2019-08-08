package com.iot.transport.handler;


import lombok.Getter;

/**
 * 客户端类型
 */
@Getter
public enum HandlerType {


        CONNECT(null),
        PUB(null),
        SUB(null),
        HEART(null);

     private DirectHandler directHandler;

     HandlerType(DirectHandler directHandler){
        this.directHandler=directHandler;
     }





}
