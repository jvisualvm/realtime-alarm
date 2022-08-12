package com.risen.realtime.framework.http;

import com.alibaba.fastjson.JSON;
import com.risen.helper.response.Result;
import com.risen.realtime.framework.http.DeviceHttpComponent;
import com.risen.realtime.framework.property.JDYBaseProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/4/26 13:47
 */
@Component
@AllArgsConstructor
public class DeviceHttpHelper {

    private DeviceHttpComponent deviceHttpComponent;

    public Object getOne(Object request, JDYBaseProperties properties) {
        Result result = deviceHttpComponent.post(request, properties);
        AtomicReference<Object> response = new AtomicReference(null);
        Optional.ofNullable(result).ifPresent(index -> {
            Optional.ofNullable(index.getData()).ifPresent(data -> {
                List<Object> deviceList = JSON.parseArray(JSON.toJSONString(data), Object.class);
                if (!CollectionUtils.isEmpty(deviceList)) {
                    response.set(deviceList.get(0));
                }
            });
        });
        return response.get();
    }


    public List<Object> getList(Object request, JDYBaseProperties properties) {
        Result result = deviceHttpComponent.post(request, properties);
        AtomicReference<List<Object>> response = new AtomicReference(null);
        Optional.ofNullable(result).ifPresent(index -> {
            Optional.ofNullable(index.getData()).ifPresent(data -> {
                List<Object> deviceList = JSON.parseArray(JSON.toJSONString(data));
                response.set(deviceList);
            });
        });
        return response.get();
    }


}
