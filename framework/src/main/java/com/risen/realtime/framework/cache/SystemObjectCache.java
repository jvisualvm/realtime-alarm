package com.risen.realtime.framework.cache;

import com.risen.helper.cache.CacheDataAbstract;
import com.risen.helper.page.PageUtil;
import com.risen.helper.thread.ThreadPoolUtil;
import com.risen.realtime.framework.event.DingDingPublisher;
import com.risen.realtime.framework.mapper.PushTaskDetailMapper;
import com.risen.realtime.framework.service.PushTaskDetailService;
import com.risen.realtime.framework.service.RedisTaskService;
import com.risen.realtime.framework.service.WindowService;
import com.risen.realtime.framework.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/4 13:22
 */
@Component
public class SystemObjectCache extends CacheDataAbstract<Integer, Object, List<Object>> {

    @Autowired
    private RedisTaskService redisTaskService;
    @Autowired
    private PushRuleCache pushRuleCache;
    @Autowired
    private PushConfigCache pushConfigCache;
    @Autowired
    private PushTaskCache pushTaskCache;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WindowService windowService;
    @Autowired
    private PushSystemCache pushSystemCache;
    @Autowired
    private ThreadPoolUtil threadPoolUtil;
    @Autowired
    private PushTaskDetailMapper pushTaskDetailMapper;
    @Autowired
    private PushTaskDetailService pushTaskDetailService;
    @Autowired
    private PageUtil pageUtil;
    @Autowired
    private DingDingPublisher dingDingPublisher;

    //设置永久缓存
    public SystemObjectCache() {
        super((long)1000000000, null, null);
    }

    @Override
    protected Object getOneByKey(Integer hashCode) {
        if (Integer.valueOf(RedisTaskService.class.hashCode()).equals(hashCode)) {
            return redisTaskService;
        } else if (Integer.valueOf(PushRuleCache.class.hashCode()).equals(hashCode)) {
            return pushRuleCache;
        } else if (Integer.valueOf(PushConfigCache.class.hashCode()).equals(hashCode)) {
            return pushConfigCache;
        } else if (Integer.valueOf(PushTaskCache.class.hashCode()).equals(hashCode)) {
            return pushTaskCache;
        } else if (Integer.valueOf(RedisUtil.class.hashCode()).equals(hashCode)) {
            return redisUtil;
        } else if (Integer.valueOf(WindowService.class.hashCode()).equals(hashCode)) {
            return windowService;
        } else if (Integer.valueOf(PushSystemCache.class.hashCode()).equals(hashCode)) {
            return pushSystemCache;
        } else if (Integer.valueOf(ThreadPoolUtil.class.hashCode()).equals(hashCode)) {
            return threadPoolUtil;
        } else if (Integer.valueOf(PushTaskDetailMapper.class.hashCode()).equals(hashCode)) {
            return pushTaskDetailMapper;
        } else if (Integer.valueOf(PushTaskDetailService.class.hashCode()).equals(hashCode)) {
            return pushTaskDetailService;
        } else if (Integer.valueOf(PageUtil.class.hashCode()).equals(hashCode)) {
            return pageUtil;
        } else if (Integer.valueOf(DingDingPublisher.class.hashCode()).equals(hashCode)) {
            return dingDingPublisher;
        }


        return new Object();
    }

    @Override
    protected void loadCache(List<Object> objects) {

    }

    /**
     * 将对象缓存起来
     */
    @Override
    @PostConstruct
    public void loadCache() {
        put(RedisTaskService.class.hashCode(), redisTaskService);
        put(PushRuleCache.class.hashCode(), pushRuleCache);
        put(PushConfigCache.class.hashCode(), pushConfigCache);
        put(PushTaskCache.class.hashCode(), pushTaskCache);
        put(RedisUtil.class.hashCode(), redisUtil);
        put(WindowService.class.hashCode(), windowService);
        put(PushSystemCache.class.hashCode(), pushSystemCache);
        put(ThreadPoolUtil.class.hashCode(), threadPoolUtil);
        put(PushTaskDetailMapper.class.hashCode(), pushTaskDetailMapper);
        put(PushTaskDetailService.class.hashCode(), pushTaskDetailService);
        put(PageUtil.class.hashCode(), pageUtil);
        put(DingDingPublisher.class.hashCode(), dingDingPublisher);

    }


}
