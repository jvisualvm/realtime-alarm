package com.risen.realtime.framework.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("push_config")
@ApiModel(value = "PushConfigEntity对象", description = "")
public class PushConfigEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    @TableField("webhook")
    private String webhook;

    @TableField("secret")
    private String secret;

    @TableField("at_all")
    private Boolean atAll;

    @TableField("mobile_list")
    private String mobileList;

    @TableField("limit_count")
    private Integer limitCount;

    @TableField("template")
    private String template;

    @TableField
    private Boolean enable;

    @TableField
    private String name;

    @TableField
    private Date createTime;

    @TableField
    private Date updateTime;
}
