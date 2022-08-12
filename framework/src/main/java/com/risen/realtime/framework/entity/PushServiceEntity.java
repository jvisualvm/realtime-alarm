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
@TableName("push_service")
@ApiModel(value = "PushServiceEntity对象", description = "")
public class PushServiceEntity {

    @TableField
    private Integer port;

    @TableField
    private String ip;

    @TableField
    private String serviceName;

    @TableField
    private String url;

    @TableField
    private String env;

    @TableField
    private String mac;

    public void updateAllField(String ip, Integer port, String serviceName, String url, String env,String mac) {
        this.port = port;
        this.ip = ip;
        this.serviceName = serviceName;
        this.url = url;
        this.env = env;
        this.mac=mac;
    }
}
