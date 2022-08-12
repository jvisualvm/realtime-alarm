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
@TableName("spc_back_passivation")
@ApiModel(value = "SpcBackPassivationEntity对象", description = "")
public class DataSpcCz302BDHEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "data_key", exist = false)
    private Long dataKey;

    @TableId("itemid")
    private Long itemid;

    @ApiModelProperty("日期")
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

    @ApiModelProperty("膜厚")
    @TableField("film_thickness")
    private Double filmThickness;

    @ApiModelProperty("平均膜厚")
    @TableField("average")
    private Double average;

    @ApiModelProperty("均匀性")
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

    @ApiModelProperty("控制上限")
    @TableField("control_upper_limit")
    private Double controlUpperLimit;

    @ApiModelProperty("控制下限")
    @TableField("control_lower_limit")
    private Double controlLowerLimit;

    @ApiModelProperty("规格上限")
    @TableField("spec_upper_limit")
    private Double specUpperLimit;

    @ApiModelProperty("规格下限")
    @TableField("spec_lower_limit")
    private Double specLowerLimit;

    @ApiModelProperty("目标中心值")
    @TableField("center_value")
    private Double centerValue;

    @TableField("data_time")
    private String dataTime;

    @ApiModelProperty("ip地址")
    @TableField("ipaddress")
    private String ipaddress;

    @ApiModelProperty("设备code")
    @TableField("device_code")
    private String deviceCode;

    @TableField("host")
    private String host;

    @TableField("lu_guan")
    private String luGuan;

    @TableField("position")
    private Integer position;


}
