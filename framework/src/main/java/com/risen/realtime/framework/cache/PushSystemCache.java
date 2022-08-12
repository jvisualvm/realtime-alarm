package com.risen.realtime.framework.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.helper.cache.CacheDataAbstract;
import com.risen.helper.config.ProfileConfig;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.framework.entity.PushSystemEntity;
import com.risen.realtime.framework.mapper.PushSystemMapper;
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
public class PushSystemCache extends CacheDataAbstract<String, PushSystemEntity, List<PushSystemEntity>> {

    @Autowired
    private ProfileConfig profileConfig;
    @Autowired
    private PushSystemMapper pushSystemMapper;

    public PushSystemCache() {
        super((long)48, null, null);
    }

    @Override
    protected PushSystemEntity getOneByKey(String ruleKey) {
        LambdaQueryWrapper<PushSystemEntity> query = new LambdaQueryWrapper<>();
        query.eq(PredicateUtil.isNotEmpty(ruleKey), PushSystemEntity::getTaskKey, ruleKey);
        query.eq(true, PushSystemEntity::getEnv, profileConfig.getActiveProfile());
        List<PushSystemEntity> lst = pushSystemMapper.selectList(query);
        if (CollectionUtils.isEmpty(lst)) {
            return new PushSystemEntity();
        }
        return lst.get(0);
    }

    @Override
    protected void loadCache(List<PushSystemEntity> pushSystemEntityList) {

    }

    @Override
    public void loadCache() {
        LambdaQueryWrapper<PushSystemEntity> query = new LambdaQueryWrapper<>();
        query.eq(true, PushSystemEntity::getEnv, profileConfig.getActiveProfile());
        List<PushSystemEntity> lst = pushSystemMapper.selectList(query);
        if (!CollectionUtils.isEmpty(lst)) {
            List<PushSystemEntity> temp = lst.stream().filter(item -> PredicateUtil.isNotEmpty(item.getTaskKey())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(temp)) {
                temp.forEach(item -> {
                    put(item.getTaskKey(), item);
                });
            }
        }
    }


}
