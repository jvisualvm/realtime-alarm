package com.risen.realtime.framework.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/21 20:09
 */
@Component
@Slf4j
public class ServiceRunSuccessListener implements ApplicationListener<AvailabilityChangeEvent> {

    public static boolean isRunning = false;

    @Override
    public void onApplicationEvent(AvailabilityChangeEvent event) {
        if (ReadinessState.ACCEPTING_TRAFFIC == event.getState()) {
            isRunning = true;
        }
    }
}
