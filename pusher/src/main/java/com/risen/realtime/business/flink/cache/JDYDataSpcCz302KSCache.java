package com.risen.realtime.business.flink.cache;

import com.alibaba.fastjson.JSON;
import com.risen.helper.cache.AgentCacheAbstract;
import com.risen.helper.util.LogUtil;
import com.risen.realtime.framework.http.DeviceHttpHelper;
import com.risen.realtime.resource.dto.SpcCz302KsJDYDetailDTO;
import com.risen.realtime.resource.dto.SpcCz302KsJDYTransferDetailDTO;
import com.risen.realtime.resource.property.JDYDataSpcCz302KSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/4/26 15:00
 */
@Component
public class JDYDataSpcCz302KSCache extends AgentCacheAbstract<String, SpcCz302KsJDYTransferDetailDTO> {

    @Autowired
    private DeviceHttpHelper deviceHttpHelper;

    @Autowired
    private JDYDataSpcCz302KSProperties jdyDataSpcCz302KSProperties;

    public JDYDataSpcCz302KSCache() {
        super(102400000, (Integer) null, (Integer) null);
    }

    @Override
    public void loadCache() {
        Object response = deviceHttpHelper.getOne(null, jdyDataSpcCz302KSProperties);
        Optional.ofNullable(response).ifPresent(item -> {
            SpcCz302KsJDYDetailDTO spc = JSON.parseObject(JSON.toJSONString(item), SpcCz302KsJDYDetailDTO.class);
            SpcCz302KsJDYTransferDetailDTO dto = new SpcCz302KsJDYTransferDetailDTO();
            dto.updateAllField(spc);
            put(createCacheKey(), dto);
            LogUtil.info("JDYDataSpcCz302KSCache loadCache:{}", JSON.toJSONString(dto));
        });
    }

    public String createCacheKey() {
        return "JDY-CZ302-KS";
    }

}
