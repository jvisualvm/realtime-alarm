package com.risen.realtime.framework.service;

import com.risen.realtime.framework.cache.TaskSliceCache;
import com.risen.realtime.framework.entity.PushTaskEntity;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/18 10:40
 */
public class TaskManagerService {

    public static void removeOrAddTask(PushTaskEntity entity) {
        //如果任务禁用了，需要删除任务,运行中
        if (!entity.getEnable() && TaskSliceCache.containTask(entity.getTaskKey())) {
            TaskSliceCache.removeTaskOnDisable(entity.getTaskKey());
        } else if (entity.getEnable() && TaskSliceCache.containTaskInDisable(entity.getTaskKey())) {
            TaskSliceCache.removeDisableTask(entity.getTaskKey());
        }
    }

}
