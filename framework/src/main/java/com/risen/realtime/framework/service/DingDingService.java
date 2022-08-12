package com.risen.realtime.framework.service;

import com.alibaba.fastjson.JSON;
import com.risen.helper.constant.Symbol;
import com.risen.helper.response.DingDingResponse;
import com.risen.helper.util.DingDingSendUtil;
import com.risen.helper.util.LogUtil;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.framework.base.SpcBaseDetail;
import com.risen.realtime.framework.cache.PushConfigCache;
import com.risen.realtime.framework.cache.PushRuleCache;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.entity.PushConfigEntity;
import com.risen.realtime.framework.entity.PushRuleEntity;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.util.MessageLimitUtil;
import com.risen.realtime.framework.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/7 11:33
 */
public class DingDingService {

    public static boolean validCanRun(PushTaskEntity pushTask, PushRuleEntity ruleEntity) {
        boolean taskEmpty = ObjectUtils.isEmpty(pushTask) || ObjectUtils.isEmpty(pushTask.getTemplate());
        boolean ruleEntityEmpty = ObjectUtils.isEmpty(ruleEntity) || ObjectUtils.isEmpty(ruleEntity.getRuleKey());
        boolean pushIdEmpty = ObjectUtils.isEmpty(pushTask.getPushId());
        if (taskEmpty || ruleEntityEmpty || pushIdEmpty) {
            LogUtil.error("该任务为空或未配置模板,未配置推送目的地信息,请检查");
            return true;
        }
        return false;
    }


    public static void pushDingDingMsg(String taskKey, String ruleKey, Function<String, String> msg) {
        PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
        PushTaskEntity pushTask = pushTaskCache.get(taskKey);
        //可能配置需要推送多个地址
        PushRuleCache pushRuleCache = SystemCacheService.getSystemObj(PushRuleCache.class);
        PushRuleEntity ruleEntity = pushRuleCache.get(ruleKey);

        //如果缺失配置，直接就不需要运行了
        if (validCanRun(pushTask, ruleEntity)) {
            return;
        }
        //构建消息体并且发送
        buildMsgAndSend(pushTask, msg.apply(pushTask.getTemplate()), false);

    }


    public static boolean spcValid(String ruleKey) {
        PushRuleCache pushRuleCache = SystemCacheService.getSystemObj(PushRuleCache.class);
        PushRuleEntity ruleEntity = pushRuleCache.get(ruleKey);
        if (ObjectUtils.isEmpty(ruleEntity) ||!ruleEntity.getEnable()||ObjectUtils.isEmpty(ruleEntity.getRuleValueInt())){
            LogUtil.error(ruleKey + "规则没有配置次数，请检查");
            return false;
        }
        return true;
    }

    public static DingDingResponse buildMsgAndSend(PushTaskEntity pushTask, String msg, boolean isRetry) {
        PushConfigCache pushConfigCache = SystemCacheService.getSystemObj(PushConfigCache.class);
        String[] pushId = pushTask.getPushId().split(Symbol.SYMBOL_COMMA);
        List<PushConfigEntity> pushConfigEntityList = new ArrayList<>();
        for (String id : pushId) {
            PushConfigEntity item = pushConfigCache.get(Integer.valueOf(id));
            if (PredicateUtil.isNotEmpty(item)) {
                pushConfigEntityList.add(item);
            }
        }
        return buildMsgAndSend(pushConfigEntityList, pushTask, msg, isRetry);
    }


    public static DingDingResponse buildMsgAndSend(List<PushConfigEntity> pushConfigEntityList, PushTaskEntity pushTask, String msg, boolean isRetry) {
        AtomicReference<DingDingResponse> result = new AtomicReference<>();
        RedisTaskService redisTaskService = SystemCacheService.getSystemObj(RedisTaskService.class);
        pushConfigEntityList.forEach(index -> {
            if (!isOverLimit(BuildTaskKey.getTaskLimitKey(pushTask.getTaskKey()), index.getId())) {
                if (index.getEnable()) {
                    List<String> mobileList = new ArrayList<>();
                    if (StringUtils.isNotEmpty(pushTask.getMobileList())) {
                        mobileList.addAll(Arrays.asList(pushTask.getMobileList().split(Symbol.SYMBOL_COMMA)));
                    } else {
                        if (StringUtils.isNotEmpty(index.getMobileList())) {
                            mobileList.addAll(Arrays.asList(index.getMobileList().split(Symbol.SYMBOL_COMMA)));
                        }
                    }
                    List<String> userIdList = new ArrayList<>();
                    if (StringUtils.isNotEmpty(pushTask.getUserList())) {
                        userIdList.addAll(Arrays.asList(pushTask.getUserList().split(Symbol.SYMBOL_COMMA)));
                    }
                    //根据任务配置的周期休眠一次
                    if (!MessageLimitUtil.taskWait(String.valueOf(index.getId()), pushTask.getPeriod())) {
                        if (!isRetry) {
                            redisTaskService.addPushHistory(pushTask, index, msg, false, "当前任务已经发送过快，已被限制发送");
                        }
                        return;
                    }

                    //在这里对每个任务进行限制次数
                    result.set(DingDingSendUtil.sendMarkdown(msg, pushTask.getTitle(), index.getSecret(), index.getWebhook(), index.getAtAll(), mobileList, userIdList));
                    if (ObjectUtils.isEmpty(result.get()) || result.get().getErrcode() != 0) {
                        //发送以后将发送历史保存到redis
                        if (!isRetry) {
                            redisTaskService.addPushHistory(pushTask, index, msg, false, JSON.toJSONString(result.get()));
                        }
                        return;
                    }
                    //发送完成以后需要减去1
                    reduceLimit(BuildTaskKey.getTaskLimitKey(pushTask.getTaskKey()), index.getId());
                    if (!isRetry) {
                        //发送以后将发送历史保存到redis
                        redisTaskService.addPushHistory(pushTask, index, msg, true, null);
                    }
                }
            } else {
                if (!isRetry) {
                    //发送以后将发送历史保存到redis
                    redisTaskService.addPushHistory(pushTask, index, msg, false, "当前任务已经超出推送次数限制");
                }
            }
        });
        return result.get();
    }


    private static boolean isOverLimit(String taskKey, Integer pushId) {
        String pushIdString = String.valueOf(pushId);
        RedisUtil redisUtil = SystemCacheService.getSystemObj(RedisUtil.class);
        //在这里查询是否超过了限制
        Object cacheValue = redisUtil.getMapValue(taskKey, pushIdString);
        if (PredicateUtil.isNotEmpty(cacheValue)) {
            int count = (Integer) cacheValue;
            if (count <= 0) {
                LogUtil.error("当前任务：{}，已经超出推送次数限制", taskKey);
                return true;
            }
        }
        return false;
    }

    /**
     * 限制减1
     *
     * @param taskKey
     * @param pushId
     */
    private static void reduceLimit(String taskKey, Integer pushId) {
        String pushIdString = String.valueOf(pushId);
        RedisUtil redisUtil = SystemCacheService.getSystemObj(RedisUtil.class);
        //在这里查询是否超过了限制
        Object cacheValue = redisUtil.getMapValue(taskKey, pushIdString);
        if (PredicateUtil.isNotEmpty(cacheValue)) {
            int count = (Integer) cacheValue - 1;
            redisUtil.setMapValue(taskKey, pushIdString, count);
        }
    }

    public static void pushDingDingMsg(String taskKey, SpcBaseDetail value, Function<String, String> msg) {
        PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
        PushTaskEntity pushTask = pushTaskCache.get(taskKey);
        //可能配置需要推送多个地址
        PushRuleCache pushRuleCache = SystemCacheService.getSystemObj(PushRuleCache.class);
        PushRuleEntity ruleEntity = pushRuleCache.get(value.getRule());
        //如果缺失配置，直接就不需要运行了
        if (DingDingService.validCanRun(pushTask, ruleEntity)) {
            return;
        }

        pushDingDingMsg(taskKey, value.getRule(), msg);

    }

}
