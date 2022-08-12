package com.risen.realtime.framework.event;

import com.risen.realtime.framework.base.SpcBaseDetail;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/13 16:15
 */
@Component
@AllArgsConstructor
public class DingDingPublisher {

    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(String taskKey, SpcBaseDetail value, Function<String, String> msg) {
        applicationEventPublisher.publishEvent(new DingDingEvent(new DingDingSource(taskKey, value, msg)));
    }

}
