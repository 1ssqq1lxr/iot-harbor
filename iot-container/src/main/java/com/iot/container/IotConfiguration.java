package com.iot.container;


import com.iot.api.RsocketMessageHandler;
import com.iot.api.client.RsocketClientSession;
import com.iot.api.server.RsocketServerSession;
import com.iot.common.annocation.ProtocolType;
import com.iot.transport.client.TransportClient;
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

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

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
    public RsocketServerSession initServer(@Autowired IotConfig iotConfig,@Autowired(required = false) AuthencationSession authencationSession
                                           ,@Autowired(required = false)  ExceptorAcceptor exceptorAcceptor,
                                           @Autowired(required = false) RsocketMessageHandler messageHandler )  {
        BiFunction<String,String,Boolean> auth = Optional.ofNullable(authencationSession)
                .map(au-> {
                    BiFunction<String,String,Boolean> consumer = (u,p)->authencationSession.auth(u,p);
                    return  consumer;
                }).orElse((u,p)->true);
        Consumer<Throwable> throwableConsumer = Optional.ofNullable(exceptorAcceptor)
                .map(ea-> {
                    Consumer<Throwable> consumer = ts->ea.accept(ts);
                    return  consumer;
                }).orElse(ts->{});
        return TransportServer.create(iotConfig.getServer().getHost(),iotConfig.getServer().getPort())
                .heart(iotConfig.getServer().getHeart())
                .log(iotConfig.getServer().isLog())
                .protocol(iotConfig.getServer().getProtocol())
                .auth(auth)
                .ssl(iotConfig.getServer().isSsl())
                .messageHandler(messageHandler)
                .exception(System.out::println)
                .start()
                .block();
    }

    @Bean
    @ConditionalOnProperty(prefix = "iot.mqtt.client",name = "enable", havingValue = "true")
    public RsocketClientSession initClient(@Autowired IotConfig iotConfig)  {
        IotConfig.Client client=iotConfig.getClient();
        MessageAcceptor messageAcceptor =this.applicationContext.getBean(MessageAcceptor.class);
        ExceptorAcceptor exceptorAcceptor =this.applicationContext.getBean(ExceptorAcceptor.class);
        OnCloseListener onCloseListener= this.applicationContext.getBean(OnCloseListener.class);
        return TransportClient.create(client.getIp(),client.getPort())
                .heart(client.getHeart())
                .protocol(client.getProtocol())
                .ssl(client.isSsl())
                .log(client.isLog())
                .clientId(client.getOption().getClientIdentifier())
                .password(client.getOption().getPassword())
                .username(client.getOption().getPassword())
                .willMessage(client.getOption().getWillMessage())
                .willTopic(client.getOption().getWillTopic())
                .willQos(client.getOption().getWillQos())
                .onClose(()-> Optional.ofNullable(onCloseListener).ifPresent(OnCloseListener::start))
                .exception(throwable->Optional.ofNullable(exceptorAcceptor).ifPresent(ec->ec.accept(throwable)))
                .messageAcceptor(messageAcceptor::accept)
                .connect()
                .block();
    }


}
