package com.risen.realtime.framework.manager;

import com.risen.helper.util.PredicateUtil;
import com.risen.helper.util.SpringBeanUtil;
import com.risen.realtime.framework.base.SpcDataMapper;
import com.risen.realtime.framework.entity.PushLocationEntity;
import com.risen.realtime.framework.service.RedisTaskService;
import com.risen.realtime.framework.service.SystemCacheService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 11:13
 */
public abstract class SpcDataJobManager<T, M extends SpcDataMapper<T>> implements Job {

    private static ConcurrentHashMap<String, SpcDataJobManager> implTree = new ConcurrentHashMap<>();

    public Class<M> mapper;

    public SpcDataJobManager() {
        implTree.put(taskKey(), this);
        mapper = (Class<M>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public abstract String taskKey();

    public abstract Integer limit();

    public abstract boolean isSystem();

    public abstract boolean runWithServiceStart();

    public abstract void fillDataAndSend(T t, RedisTaskService redisTaskService);

    public void queryDataBeforeSend() {
        RedisTaskService redisTaskService = SystemCacheService.getSystemObj(RedisTaskService.class);
        M m = SpringBeanUtil.getBean(mapper);
        long startDataKey = redisTaskService.updateInitTaskLocation(taskKey(), m);
        boolean skip = true;
        while (skip) {
            List<T> result = m.queryListByCondition(startDataKey, PredicateUtil.isNotEmpty(limit()) ? limit() : 1000);
            if (CollectionUtils.isEmpty(result)) {
                skip = false;
            } else {
                result.forEach(item -> {
                    fillDataAndSend(item, redisTaskService);
                });
                startDataKey = redisTaskService.getTaskLocation(taskKey(), PushLocationEntity.class).getStartKey();
            }
        }
    }


    @Override
    public abstract void execute(JobExecutionContext jobExecutionContext);


    public static SpcDataJobManager getImplTree(String taskKey) {
        return implTree.get(taskKey);
    }

    public static List<SpcDataJobManager> getImplTree() {
        return implTree.values().stream().collect(Collectors.toList());
    }
}
