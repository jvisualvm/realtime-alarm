package com.risen.realtime.business.flink.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/18 12:53
 */
public enum OnlineStatusEnum {

    ONLINE("on", "在线"),
    OFFLINE("off", "离线");

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String msg;

    OnlineStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
