package com.risen.realtime.framework.job;

import com.alibaba.fastjson.JSON;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.cosntant.JobTypeEnum;
import com.risen.realtime.framework.cosntant.RedisKey;
import com.risen.realtime.framework.entity.EmptyEntity;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.manager.SpcDataJobManager;
import com.risen.realtime.framework.mapper.EmptyMapper;
import com.risen.realtime.framework.service.BuildTaskKey;
import com.risen.realtime.framework.service.RedisTaskService;
import com.risen.realtime.framework.service.SystemCacheService;
import com.risen.realtime.framework.util.RedisUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/4 10:29
 */
@DisallowConcurrentExecution
@Component
public class LimitRequestCountJob extends SpcDataJobManager<EmptyEntity, EmptyMapper> {
    /**
     * 限额1分钟/20条消息也就是3秒消费一次
     *
     * @param jobExecutionContext
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
        RedisUtil redisUtil = SystemCacheService.getSystemObj(RedisUtil.class);
        //每天凌晨执行一次，分配单个任务发送钉钉的请求次数限制
        Map<String, PushTaskEntity> pushConfigEntityMap = pushTaskCache.getAllCacheValue();
        if (!CollectionUtils.isEmpty(pushConfigEntityMap)) {
            pushConfigEntityMap.forEach((k, v) -> {
                //只针对业务job
                boolean isNeed = PredicateUtil.isNotEmpty(v) && PredicateUtil.isNotEmpty(v.getLimitCountMap()) && JobTypeEnum.BUSINESS.getType().equals(v.getType());
                if (isNeed) {
                    Map<String, Integer> map = JSON.parseObject(v.getLimitCountMap(), Map.class);
                    if (!CollectionUtils.isEmpty(map)) {
                        distributionLimit(redisUtil, BuildTaskKey.getTaskLimitKey(k), map);
                    }
                }
            });
        }

    }

    private void distributionLimit(RedisUtil redisUtil, String limitKey, Map<String, Integer> map) {
        map.forEach((k, v) -> {
            redisUtil.setMapValue(limitKey, k, v);
        });
    }

    @Override
    public String taskKey() {
        return RedisKey.DISTRIB_LIMIT_TASK_KEY;
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
