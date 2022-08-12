package com.risen.realtime.framework.cosntant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/4 11:25
 */
public enum JobTypeEnum {

    SYSTEM(0, "系统定时任务"),
    BUSINESS(1, "业务定时任务"),
    LISTENER(2, "实时数据监听器");

    @Getter
    @Setter
    private Integer type;

    @Getter
    @Setter
    private String name;

    JobTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }
}
