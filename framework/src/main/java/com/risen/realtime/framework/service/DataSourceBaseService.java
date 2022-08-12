package com.risen.realtime.framework.service;

import com.risen.helper.util.LogUtil;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.framework.base.SpcDataMapper;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.cache.TaskSliceCache;
import com.risen.realtime.framework.entity.PushLocationEntity;
import com.risen.realtime.framework.entity.PushTaskEntity;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Function;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/5 16:43
 */
public class DataSourceBaseService {

    public static <T extends Object, U extends Object> void buildDataSource(String taskKey, Integer count, SpcDataMapper<T> mapper, Function<T, U> function, Function<U, Boolean> isContinue, Function<U, Boolean> isFilter, Function<List<T>, Long> maxDataKeyFunction, SourceFunction.SourceContext ctx) {
        RedisTaskService redisTaskService = SystemCacheService.getSystemObj(RedisTaskService.class);
        PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
        long startDataKey = redisTaskService.updateInitTaskLocation(taskKey, mapper);
        boolean skip = true;
        while (skip) {
            //在redis里面插入心跳标记位
            redisTaskService.setStrValue(BuildTaskKey.getTaskHeartbeatKey(taskKey), System.currentTimeMillis());
            if (TaskSliceCache.containTaskInDisable(taskKey)) {
                //清空当前任务的redis信息
                FlinkRegisterService.getImplTree(taskKey).deleteAllKeyByMatch();
                LogUtil.info("***************当前任务:{}被禁用，将退出***************", taskKey);
                break;
            }
            PushTaskEntity taskConfig = pushTaskCache.get(taskKey);
            if (PredicateUtil.isNotEmpty(taskConfig) && !taskConfig.getEnable()) {
                continue;
            }
            List<T> result = mapper.queryListByCondition(startDataKey, count);
            if (CollectionUtils.isEmpty(result)) {
                continue;
            } else {
                for (int i = 0; i < result.size(); i++) {
                    //在redis里面插入心跳标记位
                    redisTaskService.setStrValue(BuildTaskKey.getTaskHeartbeatKey(taskKey), System.currentTimeMillis());
                    if (TaskSliceCache.containTaskInDisable(taskKey)) {
                        break;
                    }
                    U u = function.apply(result.get(i));
                    if (!isContinue.apply(u)) {
                        break;
                    }
                    if (!isFilter.apply(u)) {
                        continue;
                    }
                    ctx.collect(u);
                }
                //每一条都要更新位置信息
                redisTaskService.updateRedisTaskLocation(taskKey, maxDataKeyFunction.apply(result));
                startDataKey = redisTaskService.getTaskLocation(taskKey, PushLocationEntity.class).getStartKey();
            }
        }
    }


}
