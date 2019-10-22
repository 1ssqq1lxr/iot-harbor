package com.iot.message.container;

import com.iot.api.server.handler.MemoryMessageHandler;
import com.iot.common.annocation.ProtocolType;
import com.iot.transport.client.TransportClient;
import com.iot.transport.server.TransportServer;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;


public class clientTest {

    @Test
    public void testServer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
      TransportClient.create("127.0.0.1",1884)
              .heart(100000)
              .protocol(ProtocolType.MQTT)
              .ssl(false)
              .log(true)
              .exception(throwable -> System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+throwable))
              .start()
              .subscribe();
        latch.await();



    }

}
