package com.iot.api.server.path;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.iot.api.TransportConnection;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TopicManager {


    private TopicMap<String, TransportConnection> pathMap = new TopicMap();

    private LoadingCache<String, Optional<List<TransportConnection>>> cache =
            CacheBuilder.newBuilder()//设置并发级别为8，并发级别是指可以同时写缓存的线程数
                    .concurrencyLevel(8)//设置缓存容器的初始容量为10
                    .initialCapacity(10)//设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                    .maximumSize(100)//是否需要统计缓存情况,该操作消耗一定的性能,生产环境应该去除
                    .recordStats()//设置写缓存后n秒钟过期
                    .expireAfterWrite(20, TimeUnit.MINUTES)//设置读写缓存后n秒钟过期,实际很少用到,类似于expireAfterWrite
                    .build(new CacheLoader<String, Optional<List<TransportConnection>>>() {
                        @Override
                        public Optional<List<TransportConnection>> load(String key) throws Exception {
                            String[] methodArray = key.split("/");
                            return Optional.ofNullable(pathMap.getData(methodArray));
                        }
                    });


    public Optional<List<TransportConnection>> getTopicConnection(String topic) {
        try {
            return cache.getUnchecked(topic);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    public void addTopicConnection(String topic, TransportConnection connection) {
        String[] methodArray = topic.split("/");
        pathMap.putData(methodArray, connection);
        cache.invalidate(topic);
    }


    public void deleteTopicConnection(String topic, TransportConnection connection) {
        String[] methodArray = topic.split("/");
        pathMap.delete(methodArray, connection);
        cache.invalidate(topic);
    }


}
