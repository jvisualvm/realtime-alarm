package com.risen.realtime.resource.report.analysis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangxin
 * @since 2022-06-30
 */
@Data
@TableName("data_spc_cz302_10_01_avg")
public class DataSpcCz302ZMAVGEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "data_key",exist = false)
    private Long dataKey;

    @ApiModelProperty("日期，yyyy-mm-dd")
    private String dataTime;

    @ApiModelProperty("线别")
    private String line;

    @ApiModelProperty("膜厚")
    private Double filmThickness;

    @ApiModelProperty("折射率")
    private Double refractivity;


}
