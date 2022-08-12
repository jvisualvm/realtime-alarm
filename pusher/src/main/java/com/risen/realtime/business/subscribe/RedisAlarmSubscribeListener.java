package com.risen.realtime.business.subscribe;

import com.alibaba.fastjson.JSON;
import com.risen.helper.util.LogUtil;
import com.risen.helper.websocket.notify.WsMessageNotifier;
import com.risen.helper.websocket.response.WsHeader;
import com.risen.helper.websocket.response.WsMessage;
import com.risen.realtime.framework.manager.RealtimeListener;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.contant.WsChannelEnum;
import com.risen.realtime.resource.dto.WsWebsocketDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zhangxin
 * @version 1.0
 * @date 20
 * 22/6/24 14:26
 */
@Component
public class RedisAlarmSubscribeListener extends RealtimeListener implements MessageListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @SneakyThrows
    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (canRun()) {
            //LogUtil.info("msg:{}", new String(message.getBody()));
            //WsWebsocketDTO body = (WsWebsocketDTO) redisTemplate.getValueSerializer().deserialize(message.getBody());
            //LogUtil.info("recived msg:{}", JSON.toJSONString(body));
            LogUtil.info("start send realTime alarm detail");
            //先写一个死循环用来测试
            String json = "{\n" +
                    "    \"deviceId\": \"CZ302_04_17\", \n" +
                    "    \"actionTime\": \"2022-05-30 07:57:04\", \n" +
                    "    \"clearTime\": \"2022-05-30 07:58:06\", \n" +
                    "    \"dataType\": \"激光开槽\", \n" +
                    "    \"message\": \"NO.117:IO—AOI检测硅片异常，请检查！\", \n" +
                    "    \"position\": \"ns=2;s=DRLaser/vs_AlarmID\", \n" +
                    "    \"deviceName\": \"激光SE17\"\n" +
                    "}";
            while (true) {
                Thread.sleep(1000);
                WsWebsocketDTO body1 = JSON.parseObject(json, WsWebsocketDTO.class);
                body1.setActionTime(new Date());
                body1.setClearTime(new Date());

                WsHeader header = WsHeader.build(WsChannelEnum.ALARM.getCode());
                WsMessage websocketMessage = WsMessage.build(header, body1);
                WsMessageNotifier.sendByChannel(websocketMessage);

                Thread.sleep(1000);
                WsWebsocketDTO body2 = JSON.parseObject(json, WsWebsocketDTO.class);
                body2.setActionTime(new Date());
                body2.setClearTime(new Date());
                WsMessage websocketMessage2 = WsMessage.build(header, body2);
                WsMessageNotifier.sendByChannel(websocketMessage2);
            }
        }
    }


    @Override
    public String taskKey() {
        return RedisKey.DEVICE_ALARM_SUB_TASK_KEY;
    }

    @Override
    public boolean canRun() {
        return canRun;
    }
}
