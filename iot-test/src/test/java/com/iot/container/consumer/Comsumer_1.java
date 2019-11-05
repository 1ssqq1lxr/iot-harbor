package com.iot.container.consumer;

import com.alibaba.fastjson.JSON;
import com.iot.api.client.RsocketClientSession;
import com.iot.common.annocation.ProtocolType;
import com.iot.transport.client.TransportClient;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;


public class Comsumer_1 {

    @Test
    public void testClient() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
    RsocketClientSession clientSession= TransportClient.create("192.168.100.237",1884)
              .heart(10000)
              .protocol(ProtocolType.MQTT)
              .ssl(false)
              .log(false)
              .clientId("Comsumer_1")
                .password("12")
            .username("123")
            .willMessage("Comsumer_1")
            .willTopic("/lose/Comsumer_1")
            .willQos(MqttQoS.AT_LEAST_ONCE)
              .exception(throwable -> System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+throwable))
              .messageAcceptor((topic,msg)->{
                  try {
                      System.out.println(topic+":"+ new String(msg));
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              })
              .connect()
              .block();
        Thread.sleep(5000);
        clientSession.sub("/dict/asr_resualt/test1234").subscribe();
        latch.await();



    }

}
