package com.iot.api;




public interface Config {

    void setIp(String ip);

    void setPort(int port);

    String getIp();

    int getPort();


    String getProtocol();

    boolean isSsl();

    boolean isLog();

    int  getHeart();
}
