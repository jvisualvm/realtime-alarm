package com.risen.realtime.framework.runner;

import com.risen.helper.consumer.ConditionConsumer;
import com.risen.helper.thread.ThreadPoolUtil;
import com.risen.helper.util.IfUtil;
import com.risen.helper.util.LogUtil;
import com.risen.helper.util.PredicateUtil;
import com.risen.helper.util.ThreadLocalUtil;
import com.risen.realtime.framework.cache.*;
import com.risen.realtime.framework.dto.MessageLimitDTO;
import com.risen.realtime.framework.entity.PushConfigEntity;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.service.FlinkRegisterService;
import com.risen.realtime.framework.service.SystemConfigService;
import com.risen.realtime.framework.util.MessageLimitUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 11:38
 */
@Component
@Order(value = 7)
@AllArgsConstructor
public class FlinkExecuteLoader implements CommandLineRunner {

    private ThreadPoolUtil threadPoolUtil;
    private PushServiceCache pushServiceCache;
    private PushSystemCache pushSystemCache;
    private SystemConfigService service;
    private PushTaskCache taskCache;
    private SystemObjectCache systemObjectCache;
    private PushConfigCache configCache;

    @Override
    public void run(String... args) throws Exception {
        //在内存里面给每个配置限额
        Map<Integer, PushConfigEntity> configEntityMap = configCache.getAllCacheValue();
        if (!CollectionUtils.isEmpty(configEntityMap)) {
            configEntityMap.forEach((k, v) -> {
                //给每个任务分配一个
                MessageLimitUtil.setMessageLimitMap(String.valueOf(k), new MessageLimitDTO());
            });
        }

        pushServiceCache.loadCache();
        //服务启动以后，将所有的Flink程序加入线程池
        List<FlinkRegisterService> registerServiceList = FlinkRegisterService.getImplTree();
        ThreadLocalUtil.inLocal.set(systemObjectCache);
        ConditionConsumer ifConsumer = () -> {
            registerServiceList.forEach(item -> {
                PushTaskEntity pushTask = taskCache.get(item.taskKey());
                if (ObjectUtils.isEmpty(pushTask)) {
                    LogUtil.error("任务:{}配置信息已经丢失，无法启动", item.taskKey());
                }
                if (item.runWithServiceStart() && pushTask.getEnable() && TaskSliceCache.containTask(item.taskKey())) {
                    //服务启动之前先给Flink任务分配端口和地址,将当前服务信息写入表
                    service.updateSystemConfig(item.taskKey());
                    threadPoolUtil.addTask(() -> {
                        item.execute();
                    });
                }
            });
        };
        IfUtil.goIf(ifConsumer, PredicateUtil.isNotEmpty(registerServiceList));
        pushSystemCache.loadCache();
    }


}
