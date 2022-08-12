package com.risen.realtime.framework.runner;

import com.risen.helper.cache.AgentCacheAbstract;
import com.risen.helper.cache.CacheCommonAbstract;
import com.risen.helper.cache.CacheDataAbstract;
import com.risen.helper.util.ForeachUtil;
import com.risen.realtime.framework.cache.SystemObjectCache;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 11:38
 */
@Component
@Order(value = 2)
@AllArgsConstructor
public class CacheAutoLoader implements CommandLineRunner {

    private SystemObjectCache systemObjectCache;

    @Override
    public void run(String... args) throws Exception {
        List<AgentCacheAbstract> agentCacheAbstractList = AgentCacheAbstract.getImplTree();
        Consumer<AgentCacheAbstract> agentCacheAbstractConsumer = (t) -> {
            systemObjectCache.put(t.getClass().hashCode(), t);
        };
        ForeachUtil.forEachOne(agentCacheAbstractList, agentCacheAbstractConsumer);

        List<CacheCommonAbstract> cacheCommonAbstractList = CacheCommonAbstract.getImplTree();
        Consumer<CacheCommonAbstract> cacheCommonAbstractConsumer = (t) -> {
            systemObjectCache.put(t.getClass().hashCode(), t);
        };
        ForeachUtil.forEachOne(cacheCommonAbstractList, cacheCommonAbstractConsumer);

        List<CacheDataAbstract> cacheDataAbstractList = CacheDataAbstract.getImplTree();
        Consumer<CacheDataAbstract> cacheDataAbstractConsumer = (t) -> {
            systemObjectCache.put(t.getClass().hashCode(), t);
        };
        ForeachUtil.forEachOne(cacheDataAbstractList, cacheDataAbstractConsumer);
    }


}
