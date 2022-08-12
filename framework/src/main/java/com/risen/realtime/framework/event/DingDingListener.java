package com.risen.realtime.framework.event;

import com.risen.realtime.framework.service.DingDingService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/13 16:15
 */
@Component
public class DingDingListener implements ApplicationListener<DingDingEvent> {

    @Override
    public void onApplicationEvent(DingDingEvent event) {
        DingDingSource dingDingSource = (DingDingSource) event.getSource();
        DingDingService.pushDingDingMsg(dingDingSource.getTaskKey(), dingDingSource.getValue(), dingDingSource.getMsg());
    }
}
