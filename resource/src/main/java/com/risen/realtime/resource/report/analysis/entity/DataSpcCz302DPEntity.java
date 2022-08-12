package com.risen.realtime.resource.report.analysis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("data_spc_cz302_07")
@ApiModel(value = "DataSpcCz30207Entity对象", description = "")
public class DataSpcCz302DPEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "data_key",exist = false)
    private Long dataKey;

    @ApiModelProperty("线别")
    @TableField("process_line")
    private String processLine;

    @ApiModelProperty("时间，yyyy-mm-dd hh24:mi:ss")
    @TableField("data_time")
    private String dataTime;

    @ApiModelProperty("班次，N:夜班(19:30<=T<次日7:30)，D:白班(7:30<=T<19:30)")
    @TableField("class_order")
    private String classOrder;

    @ApiModelProperty("碱抛槽体号")
    @TableField("tank_number")
    private String tankNumber;

    @ApiModelProperty("称重类型")
    @TableField("weight_type")
    private String weightType;

    @ApiModelProperty("称重_碱抛前1")
    @TableField("weight_throw_before1")
    private Float weightThrowBefore1;

    @ApiModelProperty("称重_碱抛前2")
    @TableField("weight_throw_before2")
    private Float weightThrowBefore2;

    @ApiModelProperty("称重_碱抛后1")
    @TableField("weight_throw_after1")
    private Float weightThrowAfter1;

    @ApiModelProperty("称重_碱抛后2")
    @TableField("weight_throw_after2")
    private Float weightThrowAfter2;

    @ApiModelProperty("机测")
    @TableField("machine_test")
    private Float machineTest;

    @ApiModelProperty("反射率1，单位：%")
    @TableField("avg1")
    private Float avg1;

    @ApiModelProperty("反射率2，单位：%")
    @TableField("avg2")
    private Float avg2;

    @ApiModelProperty("中心值_减重")
    @TableField("middle_reduce_weight")
    private Float middleReduceWeight;

    @ApiModelProperty("控制下限_减重")
    @TableField("control_down_weight")
    private Float controlDownWeight;

    @ApiModelProperty("控制上限_减重")
    @TableField("control_up_weight")
    private Float controlUpWeight;

    @ApiModelProperty("规格下限_减重")
    @TableField("standards_down_weight")
    private Float standardsDownWeight;

    @ApiModelProperty("规格上限_减重")
    @TableField("standards_up_weight")
    private Float standardsUpWeight;

    @ApiModelProperty("中心值_反射率")
    @TableField("middle_reflex_rate")
    private Float middleReflexRate;

    @ApiModelProperty("控制下限_反射率")
    @TableField("contrl_down_reflex_rate")
    private Float contrlDownReflexRate;

    @ApiModelProperty("控制上限_反射率")
    @TableField("contrl_up_reflex_rate")
    private Float contrlUpReflexRate;

    @ApiModelProperty("规格下限_反射率")
    @TableField("standards_down_reflex_rate")
    private Float standardsDownReflexRate;

    @ApiModelProperty("规格上限_反射率")
    @TableField("standards_up_reflex_rate")
    private Float standardsUpReflexRate;


}
