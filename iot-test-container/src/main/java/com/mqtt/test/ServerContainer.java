package com.mqtt.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import static org.springframework.boot.SpringApplication.run;
@SpringBootApplication
public class ServerContainer {

    public static void main(String[] args)  {
            ConfigurableApplicationContext run = run(ServerContainer.class, args);
    }

}
