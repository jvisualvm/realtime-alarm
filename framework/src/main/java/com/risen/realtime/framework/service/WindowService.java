package com.risen.realtime.framework.service;

import com.risen.helper.constant.Symbol;
import com.risen.helper.util.LimitLinkedList;
import com.risen.realtime.framework.base.SpcBaseDetail;
import com.risen.realtime.framework.util.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/6 15:48
 */
@Service
@AllArgsConstructor
public class WindowService {

    private RedisUtil redisUtil;

    /**
     * @param taskKey   任务标识
     * @param ruleKey   规则标识
     * @param windowKey 窗口标识
     * @param type      类型
     * @param limit     限制数组
     */
    public <T extends SpcBaseDetail> void initDataWindow(String taskKey, String ruleKey, String windowKey, String type, Integer limit) {
        String windowsKey = buildWindowKey(taskKey, ruleKey, windowKey, type);
        Object redisCacheList = redisUtil.getStrValue(windowsKey);
        //初始化窗口数据
        if (redisCacheList == null) {
            LimitLinkedList<T> windowList = new LimitLinkedList<>(limit);
            redisUtil.setStrValue(windowsKey, windowList);
        }
    }

    /**
     * 更新窗口数据
     *
     * @param taskKey
     * @param ruleKey
     * @param windowKey
     * @param type
     * @param windowList
     */
    public <T extends SpcBaseDetail> void updateDataWindow(String taskKey, String ruleKey, String windowKey, String type, LimitLinkedList<T> windowList) {
        String windowsKey = buildWindowKey(taskKey, ruleKey, windowKey, type);
        redisUtil.setStrValue(windowsKey, windowList);
    }

    public <T extends SpcBaseDetail> LimitLinkedList<T> getDataWindow(String taskKey, String ruleKey, String windowKey, String type, Integer limit) {
        String windowsKey = buildWindowKey(taskKey, ruleKey, windowKey, type);
        Object redisCache = redisUtil.getStrValue(windowsKey);
        LimitLinkedList<T> cacheWindowList = (LimitLinkedList) redisCache;
        //因为缺失了limit，需要重新设置
        LimitLinkedList<T> limitLinkedList = new LimitLinkedList<>(limit);
        if (!CollectionUtils.isEmpty(cacheWindowList)) {
            limitLinkedList.addAll(cacheWindowList);
        }
        return limitLinkedList;
    }


    private String buildWindowKey(String taskKey, String ruleKey, String windowKey, String type) {
        StringBuilder builder = new StringBuilder();
        builder.append(taskKey);
        builder.append(Symbol.SYMBOL_LOW);
        builder.append(ruleKey);
        builder.append(Symbol.SYMBOL_LOW);
        builder.append(windowKey);
        builder.append(Symbol.SYMBOL_LOW);
        builder.append(type);
        builder.append(Symbol.SYMBOL_LOW);
        return builder.toString();
    }

}
