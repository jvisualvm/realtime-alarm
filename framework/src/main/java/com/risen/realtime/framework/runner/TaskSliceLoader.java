package com.risen.realtime.framework.runner;

import com.risen.helper.util.LocalMACUtil;
import com.risen.helper.util.LogUtil;
import com.risen.helper.util.ServiceUtil;
import com.risen.realtime.framework.cache.PushServiceCache;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.cache.TaskSliceCache;
import com.risen.realtime.framework.cosntant.RedisKey;
import com.risen.realtime.framework.entity.PushServiceEntity;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.service.SystemConfigService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 11:38
 */
@Component
@Order(value = 3)
@AllArgsConstructor
public class TaskSliceLoader implements CommandLineRunner {

    private PushTaskCache pushTaskCache;
    private PushServiceCache serviceCache;
    private ServiceUtil serviceUtil;
    private SystemConfigService service;

    /**
     * 缺陷是需要提前配置好服务器信息
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        service.updateServiceConfig();
        LogUtil.info("****************开始对任务进行分片****************");
        Map<String, PushTaskEntity> pushTaskEntityList = pushTaskCache.getAllCacheValue();
        Map<String, PushServiceEntity> serviceEntityMap = serviceCache.getAllCacheValue();
        if (!CollectionUtils.isEmpty(pushTaskEntityList) && !CollectionUtils.isEmpty(serviceEntityMap)) {
            //获取服务节点
            List<String> serverList = serviceEntityMap.values().stream().sorted(Comparator.comparing(PushServiceEntity::getMac)).map(s -> s.getMac()).collect(Collectors.toList());
            int index = 0;
            for (int i = 0; i < serverList.size(); i++) {
                if (LocalMACUtil.getLocalMac().equals(serverList.get(i))) {
                    index = i;
                    break;
                }
            }

            int taskTotal = serviceEntityMap.size();
            LogUtil.info("****************当前节点总数:{}****************", taskTotal);
            //设置任务总数,对任务分片
            TaskSliceCache.setServerCount(taskTotal);
            List<String> taskList = pushTaskEntityList.values().stream().sorted(Comparator.comparing(PushTaskEntity::getTaskKey)).map(s -> s.getTaskKey()).collect(Collectors.toList());

            //不对任务重试任务进行分片
            taskList.remove(RedisKey.TASK_RESTART_KEY);
            taskList.remove(RedisKey.DISTRIB_LIMIT_TASK_KEY);

            for (int i = 0; i < taskList.size(); i++) {
                if (i % taskTotal == index) {
                    TaskSliceCache.addTask(taskList.get(i));
                }
            }
            LogUtil.info("****************分片完成！当前节点分配到的任务总数:{}****************", TaskSliceCache.size());

        }
    }


}
