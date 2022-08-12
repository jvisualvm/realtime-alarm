package com.risen.realtime.framework.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.helper.cache.CacheDataAbstract;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.listener.ServiceRunSuccessListener;
import com.risen.realtime.framework.mapper.PushTaskMapper;
import com.risen.realtime.framework.service.TaskManagerService;
import com.risen.realtime.framework.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/1 12:47
 */
@Component
public class PushTaskCache extends CacheDataAbstract<String, PushTaskEntity, List<PushTaskEntity>> {


    @Autowired
    private PushTaskMapper pushTaskMapper;
    @Autowired
    private RedisUtil redisUtil;

    public PushTaskCache() {
        super((long)48, null, null);
    }

    @Override
    protected PushTaskEntity getOneByKey(String taskKey) {
        LambdaQueryWrapper<PushTaskEntity> query = new LambdaQueryWrapper<>();
        query.eq(true, PushTaskEntity::getTaskKey, taskKey);
        List<PushTaskEntity> result = pushTaskMapper.selectList(query);
        if (CollectionUtils.isEmpty(result)) {
            return new PushTaskEntity();
        }
        PushTaskEntity task = result.get(0);

        //移除任务
        removeOrAddTask(task);

        return task;
    }

    @Override
    protected void loadCache(List<PushTaskEntity> pushConfigEntities) {

    }

    @Override
    public void loadCache() {
        List<PushTaskEntity> pushConfigEntities = pushTaskMapper.selectList(new LambdaQueryWrapper<>());
        if (!CollectionUtils.isEmpty(pushConfigEntities)) {
            List<PushTaskEntity> pushList = pushConfigEntities.stream().filter(s -> PredicateUtil.isNotEmpty(s.getTaskKey())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(pushList)) {
                pushList.forEach(item -> {
                    put(item.getTaskKey(), item);
                    //移除任务
                    removeOrAddTask(item);
                });
            }
        }
    }


    private void removeOrAddTask(PushTaskEntity entity) {
        if (!entity.getEnable() && !ServiceRunSuccessListener.isRunning) {
            //程序未启动完成，启动前
            redisUtil.deleteAllKeyByMatch(entity.getTaskKey());
        } else {
            TaskManagerService.removeOrAddTask(entity);
        }
    }

}
