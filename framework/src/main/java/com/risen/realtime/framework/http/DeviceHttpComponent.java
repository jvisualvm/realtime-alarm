package com.risen.realtime.framework.http;

import com.risen.helper.http.HttpHelper;
import com.risen.helper.response.Result;
import com.risen.realtime.framework.property.JDYBaseProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/4/29 20:31
 */
@Component
@AllArgsConstructor
public class DeviceHttpComponent {

    private HttpHelper httpHelper;


    public Result post(Object request, JDYBaseProperties properties) {
        return httpHelper.post(createUrl(properties), request, getHeader(properties));
    }


    private String createUrl(JDYBaseProperties properties) {
        return String.format(properties.getRemote(), properties.getApp(), properties.getForm());
    }

    private Map<String, String> getHeader(JDYBaseProperties properties) {
        Map<String, String> headMap = new HashMap<>(1);
        headMap.put("Authorization", "Bearer " + properties.getToken());
        return headMap;
    }


}
