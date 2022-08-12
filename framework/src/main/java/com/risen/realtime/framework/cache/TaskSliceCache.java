package com.risen.realtime.framework.cache;

import com.risen.realtime.framework.cosntant.RedisKey;
import com.risen.realtime.framework.dto.TaskSliceDTO;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/14 16:00
 */
public class TaskSliceCache {


    public static Set<String> NONE_SLICE_TASK_LIST = Collections.synchronizedSet(new HashSet<>());

    static {
        NONE_SLICE_TASK_LIST.add(RedisKey.TASK_RESTART_KEY);
        NONE_SLICE_TASK_LIST.add(RedisKey.DISTRIB_LIMIT_TASK_KEY);
    }

    private static TaskSliceDTO taskSliceCache = new TaskSliceDTO();

    public static void setTaskSliceCache(Integer serverCount, Set<String> taskKeyList) {
        taskSliceCache.update(serverCount, taskKeyList);
    }

    public static TaskSliceDTO getTaskSliceCache() {
        return taskSliceCache;
    }

    public static void setServerCount(Integer serverCount) {
        taskSliceCache.setServerCount(serverCount);
    }

    public static void setTaskList(Set<String> taskKeyList) {
        taskSliceCache.setTaskKeySet(taskKeyList);
    }

    public static void addTask(String taskKey) {
        taskSliceCache.getTaskKeySet().add(taskKey);
    }

    public static boolean containTask(String taskKey) {
        return taskSliceCache.getTaskKeySet().contains(taskKey);
    }

    public static boolean containTaskInDisable(String taskKey) {
        return taskSliceCache.getDisableSet().contains(taskKey);
    }

    public static int size() {
        return taskSliceCache.getTaskKeySet().size();
    }

    public static boolean removeCurrentHasTask(String taskKey) {
        taskSliceCache.getDisableSet().add(taskKey);
        return taskSliceCache.getTaskKeySet().remove(taskKey);
    }

    public static boolean removeDisableTask(String taskKey) {
        TaskSliceCache.addTask(taskKey);
        return taskSliceCache.getDisableSet().remove(taskKey);
    }


    public static void removeTaskOnDisable(String taskKey) {
        //如果任务禁用了，需要删除任务
        try {
            if (TaskSliceCache.containTask(taskKey)) {
                //只负责移除自己分片到的任务
                removeCurrentHasTask(taskKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
