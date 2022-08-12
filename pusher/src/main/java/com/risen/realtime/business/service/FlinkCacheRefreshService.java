package com.risen.realtime.business.service;

import com.risen.realtime.business.flink.cache.JDYDataSpcCz302KSCache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/13 13:41
 */
@Service
@AllArgsConstructor
public class FlinkCacheRefreshService {

    private JDYDataSpcCz302KSCache jdyDataSpcCz302KSCache;

    public void refreshCz302KsJdyCache() {
        jdyDataSpcCz302KSCache.loadCache();
    }


}
