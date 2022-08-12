package com.risen.realtime.resource.contant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/6/4 11:32
 */
public enum WsChannelEnum {

    ALARM("alarm", "告警频道");

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String msg;

    WsChannelEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
