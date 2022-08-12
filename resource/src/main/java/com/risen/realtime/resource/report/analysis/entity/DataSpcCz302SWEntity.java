package com.risen.realtime.resource.report.analysis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
 * @since 2022-06-30
 */
@Getter
@Setter
@TableName("data_spc_cz302_12_03")
@ApiModel(value = "DataSpcCz3021203Entity对象", description = "")
public class DataSpcCz302SWEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "data_key",exist = false)
    private Long dataKey;

    @ApiModelProperty("日期，yyyy-mm-dd")
    @TableField("data_time")
    private String dataTime;

    @ApiModelProperty("线别")
    @TableField("line")
    private String line;

    @ApiModelProperty("班次，N:夜班(19:30<=T<次日7:30)，D:白班(7:30<=T<19:30)")
    @TableField("ban")
    private String ban;

    @ApiModelProperty("道次")
    @TableField("dao")
    private String dao;

    @ApiModelProperty("9:30（21:30）湿重（增重量）")
    @TableField("time9")
    private String time9;

    @ApiModelProperty("11:30（23:30）湿重（增重量）")
    @TableField("time11")
    private String time11;

    @ApiModelProperty("13:30（1:30）湿重（增重量）")
    @TableField("time1")
    private String time1;

    @ApiModelProperty("15:30（3:30）湿重（增重量）")
    @TableField("time3")
    private String time3;

    @ApiModelProperty("17:30（5:30）湿重（增重量）")
    @TableField("time5")
    private String time5;

    @ApiModelProperty("平均值")
    @TableField("avg")
    private Double avg;

    @ApiModelProperty("目标")
    @TableField("target")
    private Double target;

    @ApiModelProperty("控制上限")
    @TableField("con_up")
    private Double conUp;

    @ApiModelProperty("控制下限")
    @TableField("con_low")
    private Double conLow;

    @ApiModelProperty("规格上限")
    @TableField("spc_up")
    private Double spcUp;

    @ApiModelProperty("规格下限")
    @TableField("spc_low")
    private String spcLow;

    @ApiModelProperty("入库时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty("称重类型")
    @TableField("weight_type")
    private String weightType;


}
