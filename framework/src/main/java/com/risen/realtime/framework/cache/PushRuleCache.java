package com.risen.realtime.framework.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.helper.cache.CacheDataAbstract;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.framework.entity.PushRuleEntity;
import com.risen.realtime.framework.mapper.PushRuleMapper;
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
public class PushRuleCache extends CacheDataAbstract<String, PushRuleEntity, List<PushRuleEntity>> {

    @Autowired
    private PushRuleMapper pushRuleMapper;

    public PushRuleCache() {
        super((long)48, null, null);
    }

    @Override
    protected PushRuleEntity getOneByKey(String ruleKey) {
        LambdaQueryWrapper<PushRuleEntity> query = new LambdaQueryWrapper<>();
        query.eq(PredicateUtil.isNotEmpty(ruleKey), PushRuleEntity::getRuleKey, ruleKey);
        List<PushRuleEntity> lst = pushRuleMapper.selectList(query);
        if (CollectionUtils.isEmpty(lst)) {
            return new PushRuleEntity();
        }
        return lst.get(0);
    }

    @Override
    protected void loadCache(List<PushRuleEntity> pushRuleEntities) {

    }

    @Override
    public void loadCache() {
        LambdaQueryWrapper<PushRuleEntity> query = new LambdaQueryWrapper<>();
        List<PushRuleEntity> lst = pushRuleMapper.selectList(query);
        if (!CollectionUtils.isEmpty(lst)) {
            List<PushRuleEntity> temp = lst.stream().filter(item -> PredicateUtil.isNotEmpty(item.getRuleKey())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(temp)) {
                temp.forEach(item -> {
                    put(item.getRuleKey(), item);
                });
            }
        }
    }
}
