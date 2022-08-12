package com.risen.realtime.framework.util;

import com.risen.helper.util.LogUtil;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.framework.dto.MessageLimitDTO;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/5 18:35
 */
public class MessageLimitUtil {

    public static Map<String, MessageLimitDTO> MESSAGE_LIMIT_MAP = new ConcurrentHashMap<>();

    public static MessageLimitDTO getMessageLimitMap(String taskKey) {
        return MESSAGE_LIMIT_MAP.get(taskKey);
    }

    public static void setMessageLimitMap(String key, MessageLimitDTO value) {
        MESSAGE_LIMIT_MAP.put(key, value);
    }


    public static boolean taskWait(String pushId, Integer period) {
        boolean canRun = true;
        if (ObjectUtils.isEmpty(period)) {
            period = 3;
        }
        long systemTime = System.currentTimeMillis();
        MessageLimitDTO beforeMessage = MessageLimitUtil.getMessageLimitMap(pushId);
        if (PredicateUtil.isNotEmpty(beforeMessage.getTime())) {
            long distance = (systemTime - beforeMessage.getTime());
            long periodMills = period * 1000;
            if (distance < periodMills) {
                try {
                    Thread.sleep(periodMills);
                } catch (Exception e) {
                    canRun = false;
                    LogUtil.info("error:{}", e.getMessage());
                } finally {
                    beforeMessage.updateTime(System.currentTimeMillis());
                }
            }
        } else {
            beforeMessage.updateTime(systemTime);
        }

        return canRun;
    }


}
