package com.iot.message.container;

import com.iot.api.server.handler.MemoryMessageHandler;
import com.iot.common.annocation.ProtocolType;
import com.iot.transport.server.TransportServer;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;


public class ServerTest {

    @Test
    public void testServer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
      TransportServer.create("127.0.0.1",1884)
              .auth((s,p)->true)
              .heart(100000)
              .protocol(ProtocolType.MQTT)
              .ssl(false)
              .auth((username,password)->true)
              .log(false)
              .messageHandler(new MemoryMessageHandler())
              .exception(throwable -> System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+throwable))
              .start()
              .subscribe();
        latch.await();



    }

}
