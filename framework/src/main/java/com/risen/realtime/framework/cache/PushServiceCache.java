package com.risen.realtime.framework.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.helper.cache.CacheDataAbstract;
import com.risen.helper.config.ProfileConfig;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.framework.entity.PushServiceEntity;
import com.risen.realtime.framework.mapper.PushServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/7 9:58
 */
@Component
public class PushServiceCache extends CacheDataAbstract<String, PushServiceEntity, List<PushServiceEntity>> {

    @Autowired
    private ProfileConfig profileConfig;
    @Autowired
    private PushServiceMapper pushServiceMapper;

    public PushServiceCache() {
        super((long)48, null, null);
    }

    @Override
    protected PushServiceEntity getOneByKey(String mac) {
        LambdaQueryWrapper<PushServiceEntity> query = new LambdaQueryWrapper<>();
        query.eq(PredicateUtil.isNotEmpty(mac), PushServiceEntity::getMac, mac);
        query.eq(true, PushServiceEntity::getEnv, profileConfig.getActiveProfile());
        List<PushServiceEntity> lst = pushServiceMapper.selectList(query);
        if (CollectionUtils.isEmpty(lst)) {
            return new PushServiceEntity();
        }
        return lst.get(0);
    }

    @Override
    protected void loadCache(List<PushServiceEntity> pushServiceEntityList) {

    }

    @Override
    public void loadCache() {
        LambdaQueryWrapper<PushServiceEntity> query = new LambdaQueryWrapper<>();
        query.eq(true, PushServiceEntity::getEnv, profileConfig.getActiveProfile());
        List<PushServiceEntity> lst = pushServiceMapper.selectList(query);
        if (!CollectionUtils.isEmpty(lst)) {
            List<PushServiceEntity> temp = lst.stream().filter(item -> PredicateUtil.isNotEmpty(item.getMac())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(temp)) {
                temp.forEach(item -> {
                    put(item.getMac(), item);
                });
            }
        }
    }


}
