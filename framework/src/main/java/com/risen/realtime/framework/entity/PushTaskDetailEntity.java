package com.risen.realtime.framework.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.risen.realtime.framework.dto.PushDetailDTO;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangxin
 * @since 2022-07-01
 */
@Getter
@Setter
@TableName("push_task_detail")
@ApiModel(value = "PushTaskDetailEntity对象", description = "")
public class PushTaskDetailEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("data")
    private String data;

    @TableField("status")
    private Boolean status;

    @TableField("config_id")
    private Integer configId;

    @TableField("retry_count")
    private Integer retryCount;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField
    private String taskKey;

    @TableField
    private String reason;

    @TableField
    private String title;

    @TableField
    private String groupName;

    @TableField
    private String config;

    public void updateAllField(PushDetailDTO pushDetailDTO) {
        this.data = pushDetailDTO.getMsg();
        this.status = pushDetailDTO.isStatus();
        this.createTime = new Date();
        this.taskKey = pushDetailDTO.getTaskKey();
        this.title = pushDetailDTO.getTitle();
        this.groupName = pushDetailDTO.getGroupName();
        this.reason = pushDetailDTO.getReason();
        this.config = JSON.toJSONString(pushDetailDTO.getPushConfig());
    }
}
