package com.risen.realtime.resource.property;

import com.alibaba.fastjson.JSON;
import com.risen.helper.util.LogUtil;
import com.risen.realtime.framework.property.JDYBaseProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/4/26 9:46
 */
@ConfigurationProperties(prefix = "device.cz302.ks")
@Component
@Data
public class JDYDataSpcCz302KSProperties extends JDYBaseProperties {


    @PostConstruct
    public void printConfig() {
        LogUtil.info("JDYDataSpcCz302KSProperties:{}", JSON.toJSONString(this));
    }
}
