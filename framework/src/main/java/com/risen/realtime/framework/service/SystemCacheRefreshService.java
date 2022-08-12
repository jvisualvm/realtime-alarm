package com.risen.realtime.framework.service;

import com.risen.realtime.framework.cache.PushChannelCache;
import com.risen.realtime.framework.cache.PushConfigCache;
import com.risen.realtime.framework.cache.PushRuleCache;
import com.risen.realtime.framework.cache.PushTaskCache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/12 13:00
 */
@AllArgsConstructor
@Service
public class SystemCacheRefreshService {

    private PushChannelCache pushChannelCache;
    private PushConfigCache pushConfigCache;
    private PushRuleCache pushRuleCache;
    private PushTaskCache pushTaskCache;

    public void refreshChannelCache() {
        pushChannelCache.loadCache();
    }

    public void refreshConfigCache() {
        pushConfigCache.loadCache();
    }

    public void refreshRuleCache() {
        pushRuleCache.loadCache();
    }

    public void refreshTaskCache() {
        pushTaskCache.loadCache();
    }

}
