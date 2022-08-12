package com.risen.realtime.framework.service;

import com.alibaba.fastjson.JSON;
import com.risen.helper.constant.Symbol;
import com.risen.helper.consumer.ConditionConsumer;
import com.risen.helper.exception.BusinessException;
import com.risen.helper.util.IfUtil;
import com.risen.helper.util.LogUtil;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.framework.base.SpcDataMapper;
import com.risen.realtime.framework.cache.PushChannelCache;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.dto.PushDetailDTO;
import com.risen.realtime.framework.entity.PushChannelEntity;
import com.risen.realtime.framework.entity.PushConfigEntity;
import com.risen.realtime.framework.entity.PushLocationEntity;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.util.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/1 21:40
 */
@Service
@AllArgsConstructor
public class RedisTaskService implements Serializable {

    private RedisUtil redisUtil;
    private PushChannelCache pushChannelCache;
    private PushTaskCache pushTaskCache;

    /**
     * 更新任务执行到哪里了
     *
     * @param taskKey
     */
    public void updateRedisTaskLocation(String taskKey, Long dataKey) {
        if (PredicateUtil.isAllNotNull(taskKey, dataKey)) {
            PushLocationEntity pushLocationEntity = new PushLocationEntity();
            pushLocationEntity.setStartKey(dataKey);
            //更新redis里面的任务执行情况,需要redis开启持久化
            pushLocationEntity.setTaskKey(taskKey);
            redisUtil.setStrValue(taskKey, pushLocationEntity);
        }
    }

    public <T extends Object> T getTaskLocation(String taskKey, Class<T> cls) {
        Object value = redisUtil.getStrValue(taskKey);
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(value), cls);
    }

    public <T extends SpcDataMapper> long updateInitTaskLocation(String taskKey, T spcDataMapper) {
        PushLocationEntity pushLocation = getTaskLocation(taskKey, PushLocationEntity.class);
        AtomicLong startDataKey = new AtomicLong(0);
        ConditionConsumer ifLocConsumer = () -> {
            startDataKey.set(pushLocation.getStartKey() + 1);
        };
        ConditionConsumer elseLocConsumer = () -> {
            long dataKey = spcDataMapper.queryMaxKey();
            startDataKey.set(dataKey);
            //更新位置信息
            updateRedisTaskLocation(taskKey, dataKey);
        };
        IfUtil.goIf(ifLocConsumer, elseLocConsumer, PredicateUtil.isNotEmpty(pushLocation));
        return startDataKey.get();
    }

    public void publishMessage(String topic, Object data) {
        redisUtil.publish(topic, data);
        LogUtil.debug("publishMessage topic:{},message:{}", topic, JSON.toJSONString(data));
    }


    public List<String> getChannel(String taskKey) {
        PushTaskEntity task = pushTaskCache.get(taskKey);
        if (ObjectUtils.isEmpty(task) || ObjectUtils.isEmpty(task.getChannelId())) {
            throw new BusinessException("当前任务没有配置推送频道,未刷新缓存或未配置");
        }
        List<String> channelList = new ArrayList<>();
        String[] channelArray = task.getChannelId().split(Symbol.SYMBOL_COMMA);
        for (String item : channelArray) {
            PushChannelEntity channelCache = pushChannelCache.get(Integer.valueOf(item));
            if (ObjectUtils.isEmpty(channelCache)) {
                throw new BusinessException("当前任务没有配置推送频道,未刷新缓存或未配置");
            }
            channelList.add(channelCache.getChannel());
        }
        return channelList;
    }


    public void publishSpcData(String taskKey, Object item, Long dataKey) {
        List<String> channelList = getChannel(taskKey);
        channelList.forEach(push -> {
            publishMessage(push, item);
            //为了防止redis停机造成错误，需要每次都更新位置
            updateRedisTaskLocation(taskKey, dataKey);
        });
    }

    public void addPushHistory(PushTaskEntity pushTask, PushConfigEntity pushConfig, String msg, boolean status, String reason) {
        PushDetailDTO pushDetailDTO = new PushDetailDTO(pushTask, pushConfig, msg, status, reason);
        redisUtil.addZset(BuildTaskKey.getTaskPushHistoryKey(pushTask.getTaskKey()), pushDetailDTO, System.currentTimeMillis());
    }

    public Set getHistoryByRange(String key, long start, long end) {
        return redisUtil.getZsetByRange(key, start, end);
    }

    public long getHistoryCount(String key, long start, long end) {
        return redisUtil.zsetCount(key, start, end);
    }

    public long removeHistoryByRange(String key, long start, long end) {
        return redisUtil.removeRangeByScore(key, start, end);
    }

    public void setStrValue(Object key, Object value) {
        redisUtil.setStrValue(key, value);
    }

    public boolean deleteKey(String key) {
        return redisUtil.deleteKey(key);
    }

    public Long deleteCollectKey(List<String> collectKey) {
        return redisUtil.deleteCollectKey(collectKey);
    }


    public Long deleteAllKeyByMatch(String keyLike) {
        return redisUtil.deleteAllKeyByMatch(keyLike);
    }


}
