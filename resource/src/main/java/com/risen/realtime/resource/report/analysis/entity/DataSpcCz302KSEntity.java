package com.risen.realtime.resource.report.analysis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@TableName("data_spc_cz302_03")
@ApiModel(value = "DataSpcCz30203Entity对象", description = "")
public class DataSpcCz302KSEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "data_key",exist = false)
    private Long dataKey;

    @ApiModelProperty("日期，yyyy-mm-dd hh24:mi")
    @TableField("data_time")
    private String dataTime;

    @ApiModelProperty("管号")
    @TableField("iot_id")
    private String iotId;

    @ApiModelProperty("温区")
    @TableField("wen_qu")
    private String wenQu;

    @ApiModelProperty("辅助设备，on:在线方阻测试仪，off：离线方阻测试仪")
    @TableField("aux_instrument")
    private String auxInstrument;

    @ApiModelProperty("p1")
    @TableField("p1")
    private Double p1;

    @ApiModelProperty("p2")
    @TableField("p2")
    private Double p2;

    @ApiModelProperty("p3")
    @TableField("p3")
    private Double p3;

    @ApiModelProperty("p4")
    @TableField("p4")
    private Double p4;

    @ApiModelProperty("p5")
    @TableField("p5")
    private Double p5;

    @ApiModelProperty("平均值，p1-p5的均值")
    @TableField("average_num")
    private Double averageNum;

    @ApiModelProperty("极差，p1-p5的最大值与最小值差值，max-min")
    @TableField("range_num")
    private Double rangeNum;

    @ApiModelProperty("均匀性，(max-min)/(2*avg)")
    @TableField("uniformity")
    private Double uniformity;

    @ApiModelProperty("备注")
    @TableField("notes")
    private String notes;

    @ApiModelProperty("入库时间")
    @TableField("create_time")
    private String createTime;


}
