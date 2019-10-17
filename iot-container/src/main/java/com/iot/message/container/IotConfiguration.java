package com.iot.message.container;


import com.iot.api.RsocketMessageHandler;
import com.iot.api.RsocketServerAbsOperation;
import com.iot.common.annocation.ProtocolType;
import com.iot.transport.server.TransportServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
//开启自动配置，注册一个IdGeneratorProperties类型的配置bean到spring容器，同普通的@EnableAsync等开关一样
@EnableConfigurationProperties(IotConfig.class)
public class IotConfiguration implements ApplicationContextAware {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Bean
    @ConditionalOnProperty(prefix = "iot.mqtt.server",name = "enable", havingValue = "true")
    public RsocketServerAbsOperation initServer(@Autowired IotConfig iotConfig)  {
        BiFunction<String,String,Boolean> auth= (u,p)->true;
        AuthencationSession authencationSession =this.applicationContext.getBean(AuthencationSession.class);
        if(authencationSession != null){
            auth = (u,p)->authencationSession.auth(u,p);
        }
        RsocketMessageHandler messageHandler =this.applicationContext.getBean(RsocketMessageHandler.class);
        return TransportServer.create(iotConfig.getServer().getHost(),iotConfig.getServer().getPort())
                .heart(iotConfig.getServer().getHeart())
                .log(iotConfig.getServer().isLog())
                .protocol(ProtocolType.MQTT)
                .auth(auth)
                .ssl(iotConfig.getServer().isSsl())
                .messageHandler(messageHandler)
                .start()
                .block();
    }




}
