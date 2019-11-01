#MQTT库


### 采用技术

> 使用开源reactor-netty库，实现MQTT server。集成了springboot autoconfig实现快速注入容器。
框架采用反应式reactor3库，是代码具有低延迟，高吞吐量等特点。

### 目前实现功能

-  qos 0,1,2完整实现
-  密码校验,以及版本校验
-  支持ssl加密
-  spring容器支持
-  channel存储,topic存储,保留消息等外部接口支持
-  MQTT 协议同时支持WS/TCP 传输,默认MQTT协议打开WS 8443端口



#### 服务端使用说明
```java
      TransportServer.create("127.0.0.1",1884)
              .heart(100000) // 心跳(单位:毫秒)
              .protocol(ProtocolType.MQTT)
              .ssl(true) // 开启ssl
              .auth((username,password)->true) // 认证用户密码
              .log(true) // 开启报文日志
              .exception(throwable -> {}) // 异常处理
              .messageHandler(new MemoryMessageHandler()) // 处理保留消息 默认走内存,可以自定义外部实现
              .start()
              .subscribe();
```




#### 客户端使用说明
```java
        RsocketClientSession clientSession= TransportClient.create("127.0.0.1",1884)
                  .heart(10000)
                  .protocol(ProtocolType.MQTT)
                  .ssl(false)
                  .log(true)
                  .clientId("Comsumer_3")
                    .password("12")
                    .username("123")
                    .willMessage("123")
                    .willTopic("/lose")
                  .exception(throwable -> System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+throwable))
                  .messageAcceptor((topic,msg)->{
                        System.out.println(topic+":"+new String(msg));
                   })
                  .connect()
                  .block();
            clientSession.sub("test").subscribe(); // 订阅
             clientSession.pub("test","Producer_3".getBytes()).subscribe();

            
```



#### 客户端使用说明
```spring 容器中使用
        

            
```






