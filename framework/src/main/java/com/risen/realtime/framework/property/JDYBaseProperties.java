package com.risen.realtime.framework.property;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/4/26 9:56
 */
@Data
@Component
public class JDYBaseProperties {
    private String app;
    private String form;
    private String token;
    private String remote;
}
