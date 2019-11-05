package com.iot.container.producer;

import com.iot.api.client.RsocketClientSession;
import com.iot.api.server.handler.MemoryMessageHandler;
import com.iot.common.annocation.ProtocolType;
import com.iot.transport.client.TransportClient;
import com.iot.transport.server.TransportServer;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;


public class Producer_1 {

    @Test
    public void testClient() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
    RsocketClientSession clientSession= TransportClient.create("192.168.100.237",1884)
              .heart(10000)
              .protocol(ProtocolType.MQTT)
              .ssl(false)
              .log(false)
              .clientId("Producer_1")
                .password("12")
            .username("123")
            .onClose(()->{})
            .willMessage("123")
            .willTopic("/lose")
              .exception(throwable -> System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+throwable))
              .messageAcceptor((topic,msg)->{
                    System.out.println(topic+":"+new String(msg));
               })
              .connect()
              .block();
        latch.await();



    }

}
