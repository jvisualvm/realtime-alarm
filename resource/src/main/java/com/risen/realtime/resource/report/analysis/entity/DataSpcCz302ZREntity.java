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
@TableName("data_spc_cz302_02")
@ApiModel(value = "DataSpcCz30202Entity对象", description = "")
public class DataSpcCz302ZREntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "data_key", exist = false)
    private Long dataKey;

    @ApiModelProperty("线别")
    @TableField("process_line")
    private String processLine;

    @ApiModelProperty("日期")
    @TableField("data_time")
    private String dataTime;

    @ApiModelProperty("班次，N:夜班(19:30<=T<次日7:30)，D:白班(7:30<=T<19:30)")
    @TableField("class_order")
    private String classOrder;

    @ApiModelProperty("槽体号")
    @TableField("tank_number")
    private String tankNumber;

    @ApiModelProperty("药液寿命")
    @TableField("liquid_life")
    private Integer liquidLife;

    @ApiModelProperty("称重类型")
    @TableField("weight_type")
    private String weightType;

    @ApiModelProperty("称重_制绒前1")
    @TableField("weight_throw_before1")
    private Float weightThrowBefore1;

    @ApiModelProperty("称重_制绒前2")
    @TableField("weight_throw_before2")
    private Float weightThrowBefore2;

    @ApiModelProperty("称重_制绒后1")
    @TableField("weight_throw_after1")
    private Float weightThrowAfter1;

    @ApiModelProperty("称重_制绒后2")
    @TableField("weight_throw_after2")
    private Float weightThrowAfter2;

    @ApiModelProperty("机测")
    @TableField("machine_test")
    private Float machineTest;

    @ApiModelProperty("反射率均值1，单位%")
    @TableField("avg1")
    private Float avg1;

    @ApiModelProperty("反射率均值2，单位%")
    @TableField("avg2")
    private Float avg2;

    @ApiModelProperty("中心值_制绒减重晶锐")
    @TableField("middle_reduce_weight_fabia")
    private Float middleReduceWeightFabia;

    @ApiModelProperty("控制上限_制绒减重晶锐")
    @TableField("control_up_weight_fabia")
    private Float controlUpWeightFabia;

    @ApiModelProperty("控制下限_制绒减重晶锐")
    @TableField("control_down_weight_fabia")
    private Float controlDownWeightFabia;

    @ApiModelProperty("规格上限_制绒减重晶锐")
    @TableField("standards_up_weight_fabia")
    private Float standardsUpWeightFabia;

    @ApiModelProperty("规格下限_制绒减重晶锐")
    @TableField("standards_down_weight_fabia")
    private Float standardsDownWeightFabia;

    @ApiModelProperty("中心值_制绒减重三峰")
    @TableField("middle_weight_three_peak")
    private Float middleWeightThreePeak;

    @ApiModelProperty("控制上限_制绒减重三峰")
    @TableField("contrl_up_weight_three_peak")
    private Float contrlUpWeightThreePeak;

    @ApiModelProperty("控制下限_制绒减重三峰")
    @TableField("contrl_down_weight_three_peak")
    private Float contrlDownWeightThreePeak;

    @ApiModelProperty("规格上限_制绒减重三峰")
    @TableField("standards_up_weight_three_peak")
    private Float standardsUpWeightThreePeak;

    @ApiModelProperty("规格下限_制绒减重三峰")
    @TableField("standards_down_weight_three_peak")
    private Float standardsDownWeightThreePeak;

    @ApiModelProperty("中心值_制绒反射率晶锐，单位%")
    @TableField("middle_reflex_rate_fabia")
    private Float middleReflexRateFabia;

    @ApiModelProperty("控制上线_制绒反射率晶锐，单位%")
    @TableField("contrl_up_reflex_rate_fabia")
    private Float contrlUpReflexRateFabia;

    @ApiModelProperty("控制下线_制绒反射率晶锐，单位%")
    @TableField("contrl_down_reflex_rate_fabia")
    private Float contrlDownReflexRateFabia;

    @ApiModelProperty("规格上线_制绒反射率晶锐，单位%")
    @TableField("standards_up_reflex_rate_fabia")
    private Float standardsUpReflexRateFabia;

    @ApiModelProperty("规格下线_制绒反射率晶锐，单位%")
    @TableField("standards_down_reflex_rate_fabia")
    private Float standardsDownReflexRateFabia;

    @ApiModelProperty("中心值_制绒反射率三峰，单位%")
    @TableField("middle_reflex_rate_three_peak")
    private Float middleReflexRateThreePeak;

    @ApiModelProperty("控制上线_制绒反射率三峰，单位%")
    @TableField("contrl_up_reflex_rate_three_peak")
    private Float contrlUpReflexRateThreePeak;

    @ApiModelProperty("控制下线_制绒反射率三峰，单位%")
    @TableField("contrl_down_reflex_rate_three_peak")
    private Float contrlDownReflexRateThreePeak;

    @ApiModelProperty("规格上线_制绒反射率三峰，单位%")
    @TableField("standards_up_reflex_rate_three_peak")
    private Float standardsUpReflexRateThreePeak;

    @ApiModelProperty("规格下线_制绒反射率三峰，单位%")
    @TableField("standards_down_reflex_rate_three_peak")
    private Float standardsDownReflexRateThreePeak;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("入库时间")
    @TableField("create_time")
    private Date createTime;


}
