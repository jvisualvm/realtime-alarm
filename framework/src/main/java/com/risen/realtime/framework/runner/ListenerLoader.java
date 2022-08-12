package com.risen.realtime.framework.runner;

import com.risen.realtime.framework.cache.TaskSliceCache;
import com.risen.realtime.framework.manager.RealtimeListener;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/14 13:07
 */
@Component
@Order(value = 10)
@AllArgsConstructor
public abstract class ListenerLoader implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        List<RealtimeListener> listenerList = RealtimeListener.getImplTree();
        if (!CollectionUtils.isEmpty(listenerList)) {
            listenerList.forEach(item -> {
                if (TaskSliceCache.containTask(item.taskKey())) {
                    item.canRun = true;
                }
            });
        }
    }
}
