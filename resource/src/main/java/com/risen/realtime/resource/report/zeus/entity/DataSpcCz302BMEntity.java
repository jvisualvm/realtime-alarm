package com.risen.realtime.resource.report.zeus.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-07-08
 */
@Data
@TableName("spc_notacoria")
@ApiModel(value = "SpcNotacoriaEntity对象", description = "")
public class DataSpcCz302BMEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "data_key", exist = false)
    private Long dataKey;

    @TableId("itemid")
    private Long itemid;

    @ApiModelProperty("日期，yyyy-mm-dd")
    @TableField("ri_qi")
    private String riQi;

    @ApiModelProperty("班次")
    @TableField("ban_ci")
    private String banCi;

    @ApiModelProperty("班组")
    @TableField("ban_zu")
    private String banZu;

    @ApiModelProperty("测试人员")
    @TableField("tester")
    private String tester;

    @ApiModelProperty("线别")
    @TableField("xian_bie")
    private String xianBie;

    @ApiModelProperty("炉管号")
    @TableField("lu_guan")
    private String luGuan;

    @ApiModelProperty("膜厚")
    @TableField("film_thickness")
    private Double filmThickness;

    @ApiModelProperty("折射率")
    @TableField("refractive_index")
    private Double refractiveIndex;

    @ApiModelProperty("膜厚均值")
    @TableField("average")
    private Double average;

    @ApiModelProperty("膜厚均匀性")
    @TableField("uniformity")
    private Double uniformity;

    @ApiModelProperty("标准差")
    @TableField("standard_deviation")
    private Double standardDeviation;

    @ApiModelProperty("cp")
    @TableField("cp")
    private Double cp;

    @ApiModelProperty("ca")
    @TableField("ca")
    private Double ca;

    @ApiModelProperty("cpk")
    @TableField("cpk")
    private Double cpk;

    @ApiModelProperty("膜厚_控制上限")
    @TableField("control_upper_limit")
    private Double controlUpperLimit;

    @ApiModelProperty("膜厚_控制下限")
    @TableField("control_lower_limit")
    private Double controlLowerLimit;

    @ApiModelProperty("膜厚_规格上限")
    @TableField("spec_upper_limit")
    private Double specUpperLimit;

    @ApiModelProperty("膜厚_规格下限")
    @TableField("spec_lower_limit")
    private Double specLowerLimit;

    @ApiModelProperty("膜厚_中心值")
    @TableField("center_value")
    private Double centerValue;

    @ApiModelProperty("入库时间")
    @TableField("data_time")
    private String dataTime;

    @ApiModelProperty("ip地址")
    @TableField("ipaddress")
    private String ipaddress;

    @ApiModelProperty("设备编号")
    @TableField("device_code")
    private String deviceCode;

    @TableField("host")
    private String host;

    @ApiModelProperty("测试位置")
    @TableField("position")
    private Integer position;


}
