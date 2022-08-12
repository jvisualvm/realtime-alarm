package com.risen.realtime.framework.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.helper.cache.CacheDataAbstract;
import com.risen.realtime.framework.entity.PushConfigEntity;
import com.risen.realtime.framework.mapper.PushConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/1 12:47
 */
@Component
public class PushConfigCache extends CacheDataAbstract<Integer, PushConfigEntity, List<PushConfigEntity>> {

    @Autowired
    private PushConfigMapper pushConfigMapper;

    public PushConfigCache() {
        super((long)48, null, null);
    }

    @Override
    protected PushConfigEntity getOneByKey(Integer id) {
        LambdaQueryWrapper<PushConfigEntity> query = new LambdaQueryWrapper<>();
        query.eq(true, PushConfigEntity::getId, id);
        List<PushConfigEntity> result = pushConfigMapper.selectList(query);
        if (CollectionUtils.isEmpty(result)) {
            return new PushConfigEntity();
        }
        return result.get(0);
    }

    @Override
    protected void loadCache(List<PushConfigEntity> pushConfigEntities) {

    }

    @Override
    public void loadCache() {
        List<PushConfigEntity> pushConfigEntities = pushConfigMapper.selectList(new LambdaQueryWrapper<>());
        if (!CollectionUtils.isEmpty(pushConfigEntities)) {
            pushConfigEntities.forEach(index -> {
                put(index.getId(), index);
            });
        }
    }

}
