#MQTT库


### 采用技术

> 使用开源reactor-netty库，实现MQTT server。集成了springboot autoconfig实现快速注入容器。
框架采用反应式reactor3库，是代码具有低延迟，高吞吐量等特点。

### 目前实现功能

-  qos 0,1,2完整实现
-  遗嘱消息,保留消息实现
-  客户端重连机制
-  密码校验,以及版本校验
-  支持ssl加密
-  spring容器支持
-  channel存储,topic存储,保留消息等外部接口支持
-  MQTT 协议同时支持WS/TCP 传输,默认MQTT协议打开WS 8443端口



#### 服务端使用说明
```java
          RsocketServerSession serverSession=TransportServer.create("192.168.100.237",1884)
                  .auth((s,p)->true)
                  .heart(100000)
                  .protocol(ProtocolType.MQTT)
                  .ssl(false)
                  .auth((username,password)->true)
                  .log(true)
                  .messageHandler(new MemoryMessageHandler())
                  .exception(throwable -> System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+throwable))
                  .start()
                  .block();
            serverSession.closeConnect("device-1").subscribe();// 关闭设备端
            List<TransportConnection> connections= serverSession.getConnections().block(); // 获取所有链接
 
```




#### 客户端使用说明
```java
        RsocketClientSession clientSession= TransportClient.create("127.0.0.1",1884)
                  .heart(10000)
                  .protocol(ProtocolType.MQTT) // 指定协议 MQTT 包含 TCP/WS 两个端口 默认WS走的8443     WS协议 仅仅启动TCP协议
                  .ssl(false)  // 开发tls加密
                  .log(true)  // 打印报文日志
                  .onClose(()->{}) // 客户端关闭事件
                  .clientId("Comsumer_3") // 客户端id
                  .password("12") // 密码
                  .username("123") // 用户名
                  .willMessage("123") // 遗嘱消息
                  .willTopic("/lose") // 遗嘱消息topic
                   .willQos(MqttQoS.AT_LEAST_ONCE) // 遗嘱消息qos
                  .exception(throwable -> System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+throwable)) // 异常处理
                  .messageAcceptor((topic,msg)->{
                        System.out.println(topic+":"+new String(msg));
                   }) // 消息接收处理
                  .connect()
                  .block();
             clientSession.sub("test").subscribe(); // 订阅
             clientSession.pub("test","Producer_3".getBytes()).subscribe(); // 发布qos0消息
             clientSession.pub("test","Producer_1".getBytes(),1).subscribe();  // 发布qos1消息
             clientSession.pub("test","Producer_1".getBytes(),true,1).subscribe();  // 发布qos1消息 保留消息
            
```



#### 客户端使用说明
```spring 容器中使用
        

            
```






