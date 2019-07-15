package com.iot.protocol.tcp.codec;

import com.iot.common.message.TransportMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

/**
 * @author lxr
 * @create 2018-05-30 15:54
 **/
public class MessageEncoder extends MessageToByteEncoder<TransportMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, TransportMessage msg, ByteBuf out) throws Exception {
        if(null == msg){
            throw new Exception("msg is null");
        }

    }
}
