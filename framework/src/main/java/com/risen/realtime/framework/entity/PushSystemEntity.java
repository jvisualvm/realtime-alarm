package com.risen.realtime.framework.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/7 9:56
 */
@Getter
@Setter
@TableName("push_system")
@ApiModel(value = "PushSystemEntity对象", description = "")
public class PushSystemEntity {

    @TableField
    private String taskKey;

    @TableField
    private String url;

    @TableField
    private Integer port;

    @TableField
    private String env;

    public void updateAllField(String taskKey, String url, Integer port, String env) {
        this.taskKey = taskKey;
        this.url = url;
        this.port = port;
        this.env = env;
    }
}
