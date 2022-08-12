package com.risen.realtime.framework.service;

import com.risen.helper.util.ThreadLocalUtil;
import com.risen.realtime.framework.cache.SystemObjectCache;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/8 20:29
 */
public class SystemCacheService {

    public static <T extends Object> T getSystemObj(Class<T> cls) {
        SystemObjectCache systemCache = (SystemObjectCache) ThreadLocalUtil.inLocal.get();
        return (T) systemCache.get(cls.hashCode());
    }


}
