package com.risen.realtime.framework.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.helper.cache.CacheDataAbstract;
import com.risen.realtime.framework.entity.PushChannelEntity;
import com.risen.realtime.framework.mapper.PushChannelMapper;
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
public class PushChannelCache extends CacheDataAbstract<Integer, PushChannelEntity, List<PushChannelEntity>> {

    @Autowired
    private PushChannelMapper pushChannelMapper;

    public PushChannelCache() {
        super((long)48, null, null);
    }

    @Override
    protected PushChannelEntity getOneByKey(Integer id) {
        LambdaQueryWrapper<PushChannelEntity> query = new LambdaQueryWrapper<>();
        query.eq(true, PushChannelEntity::getId, id);
        List<PushChannelEntity> result = pushChannelMapper.selectList(query);
        if (CollectionUtils.isEmpty(result)) {
            return new PushChannelEntity();
        }
        return result.get(0);
    }

    @Override
    protected void loadCache(List<PushChannelEntity> pushConfigEntities) {

    }

    @Override
    public void loadCache() {
        List<PushChannelEntity> pushChannelEntities = pushChannelMapper.selectList(new LambdaQueryWrapper<>());
        if (!CollectionUtils.isEmpty(pushChannelEntities)) {
            pushChannelEntities.forEach(index -> {
                put(index.getId(), index);
            });
        }
    }
}
