package com.risen.realtime.resource.report.analysis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
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
@TableName("data_spc_cz302_10_01")
@ApiModel(value = "DataSpcCz3021001Entity对象", description = "")
public class DataSpcCz302ZMEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableField(value = "data_key", exist = false)
    private Long dataKey;

    @ApiModelProperty("日期，yyyy-mm-dd")
    @TableField("data_time")
    private String dataTime;

    @ApiModelProperty("线别")
    @TableField("line")
    private String line;

    @ApiModelProperty("膜厚")
    @TableField("film_thickness")
    private Double filmThickness;

    @ApiModelProperty("折射率")
    @TableField("refractivity")
    private Double refractivity;

    @ApiModelProperty("膜厚复测")
    @TableField("film_retest")
    private Double filmRetest;

    @ApiModelProperty("折射率复测")
    @TableField("refractive_retest")
    private Double refractiveRetest;

    @ApiModelProperty("氧化硅膜厚中心值")
    @TableField("mh_yh_cn")
    private Double mhYhCn;

    @ApiModelProperty("氧化硅膜厚控制上限")
    @TableField("mh_yh_a")
    private Double mhYhA;

    @ApiModelProperty("氧化硅膜厚控制下限")
    @TableField("mh_yh_b")
    private Double mhYhB;

    @ApiModelProperty("氧化硅折射率中心值")
    @TableField("zs_yh_cn")
    private Double zsYhCn;

    @ApiModelProperty("氧化硅折射率控制上限")
    @TableField("zs_yh_a")
    private Double zsYhA;

    @ApiModelProperty("氧化硅折射率控制下限")
    @TableField("zs_yh_b")
    private Double zsYhB;

    @ApiModelProperty("炉管号")
    @TableField("iot_id")
    private String iotId;


}
