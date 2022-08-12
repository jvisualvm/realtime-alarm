package com.risen.realtime.framework.service;

import com.risen.realtime.framework.cache.PushSystemCache;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/3 14:41
 */
public abstract class FlinkRegisterService {

    private static ConcurrentHashMap<String, FlinkRegisterService> implTree = new ConcurrentHashMap<>();

    public FlinkRegisterService() {
        implTree.put(taskKey(), this);
    }

    public abstract String taskKey();

    public abstract void execute();

    public abstract boolean runWithServiceStart();

    public abstract void executeAfterDisable();

    public static FlinkRegisterService getImplTree(String taskKey) {
        return implTree.get(taskKey);
    }

    public static List<FlinkRegisterService> getImplTree() {
        return implTree.values().stream().collect(Collectors.toList());
    }

    public Configuration flinkConfig() {
        Configuration conf = new Configuration();
        PushSystemCache pushSystemCache = SystemCacheService.getSystemObj(PushSystemCache.class);
        Integer port = pushSystemCache.get(taskKey()).getPort();
        conf.setInteger(RestOptions.PORT, port);
        return conf;
    }

    public long deleteAllKeyByMatch() {
        RedisTaskService redisTaskService = SystemCacheService.getSystemObj(RedisTaskService.class);
        return redisTaskService.deleteAllKeyByMatch(taskKey());
    }


}
