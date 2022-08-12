package com.risen.realtime.framework.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/1 18:17
 */
@Getter
@Setter
@TableName("push_location")
@ApiModel(value = "PushLocationEntity对象", description = "")
public class PushLocationEntity {


    @TableField
    private String taskKey;

    @TableField
    private Long startKey;

}
