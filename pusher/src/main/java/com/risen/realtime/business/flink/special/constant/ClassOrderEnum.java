package com.risen.realtime.business.flink.special.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/8 14:26
 */
public enum ClassOrderEnum {

    WHITE("D", "白班(7:30<=T<19:30)"),
    BLACK("N", "夜班(19:30<=T<次日7:30)");

    @Getter
    @Setter
    private String code;
    @Getter
    @Setter
    private String msg;

    ClassOrderEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
