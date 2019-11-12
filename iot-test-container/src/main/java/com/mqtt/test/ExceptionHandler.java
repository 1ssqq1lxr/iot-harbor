package com.mqtt.test;

import com.iot.container.ExceptorAcceptor;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandler implements ExceptorAcceptor {
    @Override
    public void accept(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }
}
