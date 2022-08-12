package com.risen.realtime.framework.job;

import com.risen.helper.thread.ThreadPoolUtil;
import com.risen.helper.util.LogUtil;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.cache.TaskSliceCache;
import com.risen.realtime.framework.cosntant.RedisKey;
import com.risen.realtime.framework.entity.EmptyEntity;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.listener.ServiceRunSuccessListener;
import com.risen.realtime.framework.manager.SpcDataJobManager;
import com.risen.realtime.framework.mapper.EmptyMapper;
import com.risen.realtime.framework.service.BuildTaskKey;
import com.risen.realtime.framework.service.FlinkRegisterService;
import com.risen.realtime.framework.service.RedisTaskService;
import com.risen.realtime.framework.service.SystemCacheService;
import com.risen.realtime.framework.util.RedisUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/8 19:12
 */
@DisallowConcurrentExecution
@Component
public class TaskRestartJob extends SpcDataJobManager<EmptyEntity, EmptyMapper> {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        if (ServiceRunSuccessListener.isRunning) {
            RedisUtil redisUtil = SystemCacheService.getSystemObj(RedisUtil.class);
            ThreadPoolUtil threadPoolUtil = SystemCacheService.getSystemObj(ThreadPoolUtil.class);
            PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
            PushTaskEntity pushTaskEntity = pushTaskCache.get(taskKey());
            List<FlinkRegisterService> implTreeList = FlinkRegisterService.getImplTree();

            //默认30秒
            long time = 120 * 1000;
            if (PredicateUtil.isNotEmpty(pushTaskEntity.getPeriod())) {
                time = (pushTaskEntity.getPeriod() + 60) * 1000;
            }
            if (!CollectionUtils.isEmpty(implTreeList)) {
                long finalTime = time;
                implTreeList.forEach(item -> {
                    //只负责重启自己分到的任务
                    if (TaskSliceCache.containTask(item.taskKey())) {
                        Object timestamp = redisUtil.getStrValue(BuildTaskKey.getTaskHeartbeatKey(item.taskKey()));
                        boolean enable = pushTaskCache.get(item.taskKey()).getEnable();
                        if (enable && item.runWithServiceStart()) {
                            if (ObjectUtils.isEmpty(timestamp)) {
                                try {
                                    Thread.sleep(finalTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Object waitTimestamp = redisUtil.getStrValue(BuildTaskKey.getTaskHeartbeatKey(item.taskKey()));
                                //证明任务没运行起来，可能哪里报错了，需要重启一下
                                if (ObjectUtils.isEmpty(waitTimestamp)) {
                                    LogUtil.info("任务：{},正在被初次重启", item.taskKey());
                                    threadPoolUtil.addTask(() -> {
                                        item.execute();
                                    });
                                }
                            } else if (System.currentTimeMillis() - (Long) timestamp >= finalTime) {
                                LogUtil.info("任务：{}曾停止过，正在被重启", item.taskKey());
                                //证明任务挂掉了，需要重启
                                threadPoolUtil.addTask(() -> {
                                    item.execute();
                                });
                            }
                        }
                    }
                });
            }
        }
    }


    @Override
    public String taskKey() {
        return RedisKey.TASK_RESTART_KEY;
    }

    @Override
    public Integer limit() {
        return null;
    }

    @Override
    public boolean isSystem() {
        return true;
    }

    @Override
    public boolean runWithServiceStart() {
        return true;
    }

    @Override
    public void fillDataAndSend(EmptyEntity entity, RedisTaskService redisTaskService) {

    }


}
