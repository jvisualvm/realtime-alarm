package com.risen.realtime.framework.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/13 16:14
 */

public class DingDingEvent extends ApplicationEvent {

    public DingDingEvent(DingDingSource source) {
        super(source);
    }

}
