package com.risen.realtime.framework.manager;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/14 13:07
 */
@Component
public abstract class RealtimeListener {

    public boolean canRun = false;

    private static ConcurrentHashMap<String, RealtimeListener> implTree = new ConcurrentHashMap<>();


    public RealtimeListener() {
        implTree.put(taskKey(), this);
    }


    public abstract String taskKey();

    public abstract boolean canRun();


    public static RealtimeListener getImplTree(String taskKey) {
        return implTree.get(taskKey);
    }

    public static List<RealtimeListener> getImplTree() {
        return implTree.values().stream().collect(Collectors.toList());
    }


}
