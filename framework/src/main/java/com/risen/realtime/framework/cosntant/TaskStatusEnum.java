package com.risen.realtime.framework.cosntant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/12 13:30
 */
public enum TaskStatusEnum {

    SUCCESS(1),
    FAIL(0);

    @Getter
    @Setter
    private Integer code;


    TaskStatusEnum(Integer code) {
        this.code = code;
    }

}
