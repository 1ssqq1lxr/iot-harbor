package com.iot.common.util;

import com.iot.common.codec.ClientType;
import com.iot.common.codec.ProtocolCatagory;

/**
 * @Auther: lxr
 * @Date: 2018/12/28 10:11
 * @Description:
 *  {
 *
 *  }
 */
public class MessageUtils {



    public  static  ClientType  obtainHigh(byte b){
        switch ((b>>4 & 0xFF)){
            case 1:
                return ClientType.Web;
            case 2:
                return ClientType.Android;
            case 3:
                return ClientType.Ios;
            default:
                return ClientType.Other;
        }
    }


    public  static ProtocolCatagory obtainLow(byte b) {
        switch (b & 0x0F) {
            case 0:
                return ProtocolCatagory.ONLINE;
            case 14:
                return ProtocolCatagory.PING;
            case 15:
                return ProtocolCatagory.PONG;
            default:
                throw new RuntimeException("不支持 protocol");
        }
    }


}
