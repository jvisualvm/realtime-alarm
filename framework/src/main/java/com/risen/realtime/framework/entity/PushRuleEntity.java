package com.risen.realtime.framework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("push_rule")
@ApiModel(value = "PushRuleEntity对象", description = "")
public class PushRuleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("rule_name")
    private String ruleName;

    @TableField("rule_value_int")
    private Integer ruleValueInt;

    @TableField("rule_value_str")
    private String ruleValueStr;

    @TableField("rule_key")
    private String ruleKey;

    @TableField("remark")
    private String remark;

    @TableField("enable")
    private Boolean enable;

    @TableField
    private Date createTime;

    @TableField
    private Date updateTime;
}
