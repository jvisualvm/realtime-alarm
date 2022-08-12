package com.risen.realtime.framework.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@TableName("push_channel")
@ApiModel(value = "PushChannelEntity对象", description = "")
public class PushChannelEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;

    @TableField
    private String channel;

    @TableField
    private String channelName;

}
