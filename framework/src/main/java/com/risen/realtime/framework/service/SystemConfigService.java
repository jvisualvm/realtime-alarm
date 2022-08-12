package com.risen.realtime.framework.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.helper.config.ProfileConfig;
import com.risen.helper.constant.Symbol;
import com.risen.helper.consumer.ConditionConsumer;
import com.risen.helper.util.IfUtil;
import com.risen.helper.util.LocalMACUtil;
import com.risen.helper.util.QueryDataUtil;
import com.risen.helper.util.ServiceUtil;
import com.risen.realtime.framework.cache.PushServiceCache;
import com.risen.realtime.framework.entity.PushServiceEntity;
import com.risen.realtime.framework.entity.PushSystemEntity;
import com.risen.realtime.framework.mapper.PushServiceMapper;
import com.risen.realtime.framework.mapper.PushSystemMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/7 11:11
 */
@Component
@AllArgsConstructor
public class SystemConfigService {

    private PushSystemMapper pushSystemMapper;
    private PushServiceMapper pushServiceMapper;
    private ServiceUtil serviceUtil;
    private PushServiceCache pushServiceCache;
    private ProfileConfig profileConfig;

    public void updateServiceConfig() {
        LambdaQueryWrapper<PushServiceEntity> query = new LambdaQueryWrapper<>();
        query.eq(true, PushServiceEntity::getMac, LocalMACUtil.getLocalMac());
        query.eq(true, PushServiceEntity::getEnv, profileConfig.getActiveProfile());

        List<PushServiceEntity> resultList = pushServiceMapper.selectList(query);
        if (CollectionUtils.isEmpty(resultList)) {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(Symbol.SYMBOL_URL);
            urlBuilder.append(serviceUtil.getServiceIp());
            urlBuilder.append(Symbol.SYMBOL_COLON);
            urlBuilder.append(serviceUtil.getServicePort());
            urlBuilder.append(serviceUtil.getServiceName());
            PushServiceEntity pushServiceEntity = new PushServiceEntity();
            pushServiceEntity.updateAllField(serviceUtil.getServiceIp(), serviceUtil.getServicePort(), serviceUtil.getServiceName(), urlBuilder.toString(), profileConfig.getActiveProfile(), LocalMACUtil.getLocalMac());
            pushServiceMapper.insert(pushServiceEntity);
        }
        pushServiceCache.loadCache();
    }

    public void updateSystemConfig(String taskKey) {
        LambdaQueryWrapper<PushSystemEntity> query = new LambdaQueryWrapper<>();
        query.eq(true, PushSystemEntity::getTaskKey, taskKey);
        query.eq(true, PushSystemEntity::getEnv, profileConfig.getActiveProfile());
        List<PushSystemEntity> resultList = pushSystemMapper.selectList(query);
        if (CollectionUtils.isEmpty(resultList)) {
            LambdaQueryWrapper<PushSystemEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(true, PushSystemEntity::getEnv, profileConfig.getActiveProfile());
            lambdaQueryWrapper.orderByAsc(PushSystemEntity::getPort).last(QueryDataUtil.limit(1));
            List<PushSystemEntity> dataList = pushSystemMapper.selectList(lambdaQueryWrapper);
            PushServiceEntity pushServiceEntity = pushServiceCache.get(serviceUtil.getServiceIp());
            ConditionConsumer ifConsumer = () -> {
                updateSystemPort(taskKey, pushServiceEntity.getPort() - 1);
            };
            ConditionConsumer ConditionConsumer = () -> {
                PushSystemEntity pushSystem = dataList.get(0);
                updateSystemPort(taskKey, pushSystem.getPort() - 1);
            };
            IfUtil.goIf(ifConsumer, ConditionConsumer, CollectionUtils.isEmpty(dataList));
        }
    }

    public void updateSystemPort(String taskKey, Integer port) {
        StringBuilder builder = new StringBuilder();
        builder.append(Symbol.SYMBOL_URL);
        builder.append(serviceUtil.getServiceIp());
        builder.append(Symbol.SYMBOL_COLON);
        PushSystemEntity pushSystem = new PushSystemEntity();
        pushSystem.setPort(port);
        builder.append(pushSystem.getPort());
        pushSystem.setTaskKey(taskKey);
        pushSystem.setUrl(builder.toString());
        pushSystem.setEnv(profileConfig.getActiveProfile());
        pushSystemMapper.insert(pushSystem);
    }


}
