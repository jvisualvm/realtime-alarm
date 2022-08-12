package com.risen.realtime.framework.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/4 10:22
 */
@Data
public class SpcBaseDetail implements Serializable {
    public SpcBaseDetail() {

    }

    @ApiModelProperty("触发告警规则标识符")
    private String rule;

    @ApiModelProperty("触发告警原因")
    private String reason;

    @ApiModelProperty("工序：制绒 – 减重/反射率/Cpk")
    private String workProcedure;

    @ApiModelProperty("任务标识符")
    private String taskKey;


    public void updateKeyDetailWithTask(String taskKey, String rule, String workProcedure, String reason) {
        this.taskKey = taskKey;
        this.rule = rule;
        this.reason = reason;
        this.workProcedure = workProcedure;
    }

    public void updateKeyDetailNotTask(String rule, String workProcedure, String reason) {
        this.rule = rule;
        this.reason = reason;
        this.workProcedure = workProcedure;
    }
}
