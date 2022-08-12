package com.risen.realtime.framework.dto;

import lombok.Data;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/5 18:39
 */
@Data
public class MessageLimitDTO {

    private String taskKey;

    private Long time;


    public void updateTime(Long time) {
        this.time = time;
    }
}
