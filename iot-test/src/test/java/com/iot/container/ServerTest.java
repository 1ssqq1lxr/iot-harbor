package com.iot.container;

import com.iot.api.TransportConnection;
import com.iot.api.server.RsocketServerSession;
import com.iot.api.server.handler.MemoryMessageHandler;
import com.iot.common.annocation.ProtocolType;
import com.iot.transport.server.TransportServer;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;


public class ServerTest {

    @Test
    public void testServer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
          RsocketServerSession serverSession=TransportServer.create("192.168.100.237",1884)
                  .auth((s,p)->true)
                  .heart(100000)
                  .protocol(ProtocolType.MQTT)
                  .ssl(false)
                  .auth((username,password)->true)
                  .log(false)
                  .messageHandler(new MemoryMessageHandler())
                  .exception(throwable -> System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+throwable))
                  .start()
                  .block();
            serverSession.closeConnect("device-1").subscribe();// 关闭设备端
            List<TransportConnection> connections= serverSession.getConnections().block(); // 获取所有链接
        latch.await();



    }

}
