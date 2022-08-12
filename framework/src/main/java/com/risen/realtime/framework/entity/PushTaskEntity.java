package com.risen.realtime.framework.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("push_task")
@ApiModel(value = "PushConfigEntity对象", description = "")
public class PushTaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField
    private String taskName;

    @TableField
    private String taskKey;

    @TableField
    private String cron;

    @TableField
    private String pushId;

    @TableField
    private String channelId;

    @TableField
    private String template;

    @TableField
    private String title;

    @TableField
    private String workShop;

    @TableField
    private String limitCountMap;

    @TableField
    private Integer type;

    //频次，多久执行一次
    @TableField
    private Integer period;

    @TableField
    private Date createTime;

    @TableField
    private Date updateTime;

    @TableField
    private Boolean enable;

    @TableField
    private Integer externInt;

    @TableField
    private String externStr;

    @TableField("mobile_list")
    private String mobileList;

    private String userList;

}
