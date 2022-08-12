package com.risen.realtime.framework.manager;

import com.risen.helper.exception.BusinessException;
import com.risen.helper.quartz.manager.QuartzManager;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.entity.PushTaskEntity;
import lombok.AllArgsConstructor;
import org.quartz.Job;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 11:12
 */
@Component
@AllArgsConstructor
public class JobManager {

    private QuartzManager quartzManager;
    private PushTaskCache pushTaskCache;

    public void startJob(String taskKey, Class<? extends Job> cls) {
        PushTaskEntity cache = pushTaskCache.get(taskKey);
        if (ObjectUtils.isEmpty(cache) || ObjectUtils.isEmpty(cache.getCron())) {
            throw new BusinessException("没有配置该任务或未配置执行周期" + taskKey);
        }
        quartzManager.addCronJob(taskKey, cls, cache.getCron(), null);
    }

    public void removeJob(String jobName) {
        quartzManager.removeJob(jobName);
    }

}
