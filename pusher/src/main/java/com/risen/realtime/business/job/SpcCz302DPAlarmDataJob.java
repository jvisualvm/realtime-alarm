package com.risen.realtime.business.job;

import com.risen.realtime.framework.cache.TaskSliceCache;
import com.risen.realtime.framework.manager.SpcDataJobManager;
import com.risen.realtime.framework.service.RedisTaskService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302DPDetailDTO;
import com.risen.realtime.resource.report.analysis.entity.DataSpcCz302DPEntity;
import com.risen.realtime.resource.report.analysis.mapper.DataSpcCz302DPMapper;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/1 18:31
 */
@DisallowConcurrentExecution
@Component
public class SpcCz302DPAlarmDataJob extends SpcDataJobManager<DataSpcCz302DPEntity, DataSpcCz302DPMapper> {

    /**
     * 1. 每次执行任务，先看redis是否存在，存在就用，不存在就需要查询最大dataKey,然后更新到redis
     *
     * @param jobExecutionContext
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        if(TaskSliceCache.containTask(taskKey())) {
            queryDataBeforeSend();
        }
    }

    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_DP_TASK_KEY;
    }

    @Override
    public Integer limit() {
        return 1000;
    }

    @Override
    public void fillDataAndSend(DataSpcCz302DPEntity dataSpcCz30207, RedisTaskService redisTaskService) {
        SpcCz302DPDetailDTO spc207DetailDTO = new SpcCz302DPDetailDTO(dataSpcCz30207);
        redisTaskService.publishSpcData(taskKey(), spc207DetailDTO, dataSpcCz30207.getDataKey());
    }

    @Override
    public boolean isSystem() {
        return false;
    }

    @Override
    public boolean runWithServiceStart() {
        return false;
    }
}



