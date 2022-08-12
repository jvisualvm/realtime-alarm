package com.risen.realtime.framework.dto;

import com.risen.realtime.framework.entity.PushConfigEntity;
import com.risen.realtime.framework.entity.PushTaskEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/7 12:09
 */
@Data
public class PushDetailDTO implements Serializable {

    public PushDetailDTO() {
    }

    private String msg;
    private String title;
    private String groupName;
    private boolean status;
    private String reason;
    private PushConfigEntity pushConfig;
    private String taskKey;

    public PushDetailDTO(PushTaskEntity pushTask, PushConfigEntity pushConfig, String msg, boolean status, String reason) {
        this.msg = msg;
        this.title = pushTask.getTitle();
        this.taskKey = pushTask.getTaskKey();
        this.groupName = pushConfig.getName();
        this.status = status;
        this.reason = reason;
        this.pushConfig = pushConfig;
    }
}
