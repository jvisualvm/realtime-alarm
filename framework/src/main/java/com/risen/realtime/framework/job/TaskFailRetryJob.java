package com.risen.realtime.framework.job;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.risen.helper.constant.Symbol;
import com.risen.helper.page.PageUtil;
import com.risen.helper.response.DingDingResponse;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.cache.TaskSliceCache;
import com.risen.realtime.framework.cosntant.RedisKey;
import com.risen.realtime.framework.cosntant.TaskStatusEnum;
import com.risen.realtime.framework.entity.EmptyEntity;
import com.risen.realtime.framework.entity.PushConfigEntity;
import com.risen.realtime.framework.entity.PushTaskDetailEntity;
import com.risen.realtime.framework.listener.ServiceRunSuccessListener;
import com.risen.realtime.framework.manager.SpcDataJobManager;
import com.risen.realtime.framework.mapper.EmptyMapper;
import com.risen.realtime.framework.mapper.PushTaskDetailMapper;
import com.risen.realtime.framework.service.DingDingService;
import com.risen.realtime.framework.service.RedisTaskService;
import com.risen.realtime.framework.service.SystemCacheService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/7 13:15
 */
@DisallowConcurrentExecution
@Component
public class TaskFailRetryJob extends SpcDataJobManager<EmptyEntity, EmptyMapper> {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        if (TaskSliceCache.containTask(taskKey()) && ServiceRunSuccessListener.isRunning) {
            try {
                PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
                PushTaskDetailMapper pushTaskDetailMapper = SystemCacheService.getSystemObj(PushTaskDetailMapper.class);
                PageUtil pageUtil = SystemCacheService.getSystemObj(PageUtil.class);
                LambdaQueryWrapper<PushTaskDetailEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(true, PushTaskDetailEntity::getStatus, TaskStatusEnum.FAIL.getCode());
                Integer pageIndex = 1;
                Integer pageSize = 1000;
                boolean isContinue = true;
                while (isContinue) {
                    PageInfo<PushTaskDetailEntity> pageInfo = pageUtil.page(pageIndex, pageSize, pushTaskDetailMapper, lambdaQueryWrapper);

                    if (!CollectionUtils.isEmpty(pageInfo.getList())) {
                        pageInfo.getList().forEach(item -> {
                            PushConfigEntity config = JSON.parseObject(item.getConfig(), PushConfigEntity.class);
                            List<String> mobileList = new ArrayList<>();
                            if (StringUtils.isNotEmpty(config.getMobileList())) {
                                mobileList.addAll(Arrays.asList(config.getMobileList().split(Symbol.SYMBOL_COMMA)));
                            }
                            if (PredicateUtil.isNotEmpty(item.getRetryCount()) && item.getRetryCount() > 0) {
                                DingDingResponse dingResponse = DingDingService.buildMsgAndSend(pushTaskCache.get(item.getTaskKey()), item.getData(), true);
                                if (dingResponse.getErrcode() == 0) {
                                    item.setStatus(true);
                                    item.setUpdateTime(new Date());
                                    item.setRetryCount(0);
                                    pushTaskDetailMapper.updateById(item);
                                } else {
                                    item.setUpdateTime(new Date());
                                    item.setRetryCount(item.getRetryCount() - 1);
                                    pushTaskDetailMapper.updateById(item);
                                }
                            }
                        });
                    } else {
                        isContinue = false;
                    }
                    ++pageIndex;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String taskKey() {
        return RedisKey.TASK_FAIL_RETRY_KEY;
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
