package com.risen.realtime.framework.service;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/4 11:30
 */
public class BuildTaskKey {
    //distrib_limit_task_key taskKey {}

    public static String getTaskLimitKey(String limitKey) {
        StringBuilder builder = new StringBuilder();
        builder.append(limitKey);
        builder.append("_map");
        return builder.toString();
    }


    public static String getTaskPushHistoryKey(String taskKey) {
        StringBuilder builder = new StringBuilder();
        builder.append(taskKey);
        builder.append("_history");
        return builder.toString();
    }


    public static String getTaskHeartbeatKey(String taskKey) {
        StringBuilder builder = new StringBuilder();
        builder.append(taskKey);
        builder.append("_heartbeat");
        return builder.toString();
    }
}
