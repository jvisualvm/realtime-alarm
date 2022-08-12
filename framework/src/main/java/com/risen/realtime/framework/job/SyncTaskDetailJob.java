package com.risen.realtime.framework.job;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.risen.helper.consumer.ConditionConsumer;
import com.risen.helper.util.DateUtil;
import com.risen.helper.util.IfUtil;
import com.risen.realtime.framework.cache.TaskSliceCache;
import com.risen.realtime.framework.cosntant.RedisKey;
import com.risen.realtime.framework.dto.PushDetailDTO;
import com.risen.realtime.framework.entity.EmptyEntity;
import com.risen.realtime.framework.entity.PushTaskDetailEntity;
import com.risen.realtime.framework.manager.SpcDataJobManager;
import com.risen.realtime.framework.mapper.EmptyMapper;
import com.risen.realtime.framework.service.*;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/7 13:15
 */
@DisallowConcurrentExecution
@Component
public class SyncTaskDetailJob extends SpcDataJobManager<EmptyEntity, EmptyMapper> {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        if (TaskSliceCache.containTask(taskKey())) {
            List<FlinkRegisterService> implTreeList = FlinkRegisterService.getImplTree();
            if (!CollectionUtils.isEmpty(implTreeList)) {
                implTreeList.forEach(item -> {
                    syncTaskDetail(item.taskKey());
                });
            }
        }
    }


    public void syncTaskDetail(String task) {
        RedisTaskService redisTaskService = SystemCacheService.getSystemObj(RedisTaskService.class);
        String taskHistoryKey = BuildTaskKey.getTaskPushHistoryKey(task);
        long currentMill = System.currentTimeMillis();
        long total = redisTaskService.getHistoryCount(taskHistoryKey, DateUtil.getZeroTime(), currentMill);
        ConditionConsumer ifConsumer = () -> {
            Set<Object> cacheList = redisTaskService.getHistoryByRange(taskHistoryKey, DateUtil.getZeroTime(), currentMill);
            if (!CollectionUtils.isEmpty(cacheList)) {
                List<PushDetailDTO> pushDetailDTOList = cacheList.stream().map(s -> {
                    PushDetailDTO pushDetailDTO = JSON.parseObject(JSON.toJSONString(s), PushDetailDTO.class);
                    return pushDetailDTO;
                }).collect(Collectors.toList());
                List<List<PushDetailDTO>> partitionList = Lists.partition(pushDetailDTOList, 2000);
                insertBatch(partitionList);
            }
            redisTaskService.removeHistoryByRange(taskHistoryKey, DateUtil.getZeroTime(), currentMill);
        };
        ConditionConsumer elseConsumer = () -> {
        };
        IfUtil.goIf(ifConsumer, elseConsumer, total > 0);
    }

    private void insertBatch(List<List<PushDetailDTO>> partitionList) {
        PushTaskDetailService pushTaskDetailService = SystemCacheService.getSystemObj(PushTaskDetailService.class);
        partitionList.forEach(item -> {
            pushTaskDetailService.saveBatch(item.stream().map(s -> {
                        PushTaskDetailEntity pushTaskDetail = new PushTaskDetailEntity();
                        pushTaskDetail.updateAllField(s);
                        return pushTaskDetail;
                    }
            ).collect(Collectors.toList()));
        });
    }


    @Override
    public String taskKey() {
        return RedisKey.SYNC_DATA_TASK_KEY;
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
    public void fillDataAndSend(EmptyEntity entity, RedisTaskService redisTaskService) {

    }

    @Override
    public boolean runWithServiceStart() {
        return true;
    }
}
