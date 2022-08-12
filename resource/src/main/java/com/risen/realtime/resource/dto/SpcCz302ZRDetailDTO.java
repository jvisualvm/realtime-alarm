package com.risen.realtime.resource.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.risen.helper.util.NumberUtil;
import com.risen.realtime.framework.base.SpcBaseDetail;
import com.risen.realtime.resource.report.analysis.entity.DataSpcCz302ZREntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import scala.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangxin
 * @since 2022-06-30
 */
@Data
public class SpcCz302ZRDetailDTO extends SpcBaseDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    public SpcCz302ZRDetailDTO() {
    }

    @ApiModelProperty("线别")
    private String processLine;

    @ApiModelProperty("日期")
    private String dataTime;

    @ApiModelProperty("班次，N:夜班(19:30<=T<次日7:30)，D:白班(7:30<=T<19:30)")
    private String classOrder;

    @ApiModelProperty("槽体号")
    private String tankNumber;

    @ApiModelProperty("称重平均值")
    private Float avgWeight;

    @ApiModelProperty("反射率均值，单位%")
    private Float avgFlex;

    @ApiModelProperty("中心值_制绒减重晶锐")
    private Float middleReduceWeightFabia;

    @ApiModelProperty("控制上限_制绒减重晶锐")
    private Float controlUpWeightFabia;

    @ApiModelProperty("控制下限_制绒减重晶锐")
    private Float controlDownWeightFabia;

    @ApiModelProperty("规格上限_制绒减重晶锐")
    private Float standardsUpWeightFabia;

    @ApiModelProperty("规格下限_制绒减重晶锐")
    private Float standardsDownWeightFabia;

    @ApiModelProperty("中心值_制绒减重三峰")
    private Float middleWeightThreePeak;

    @ApiModelProperty("控制上限_制绒减重三峰")
    private Float contrlUpWeightThreePeak;

    @ApiModelProperty("控制下限_制绒减重三峰")
    private Float contrlDownWeightThreePeak;

    @ApiModelProperty("规格上限_制绒减重三峰")
    private Float standardsUpWeightThreePeak;

    @ApiModelProperty("规格下限_制绒减重三峰")
    private Float standardsDownWeightThreePeak;

    @ApiModelProperty("中心值_制绒反射率晶锐，单位%")
    private Float middleReflexRateFabia;

    @ApiModelProperty("控制上线_制绒反射率晶锐，单位%")
    private Float contrlUpReflexRateFabia;

    @ApiModelProperty("控制下线_制绒反射率晶锐，单位%")
    private Float contrlDownReflexRateFabia;

    @ApiModelProperty("规格上线_制绒反射率晶锐，单位%")
    private Float standardsUpReflexRateFabia;

    @ApiModelProperty("规格下线_制绒反射率晶锐，单位%")
    private Float standardsDownReflexRateFabia;

    @ApiModelProperty("中心值_制绒反射率三峰，单位%")
    private Float middleReflexRateThreePeak;

    @ApiModelProperty("控制上线_制绒反射率三峰，单位%")
    private Float contrlUpReflexRateThreePeak;

    @ApiModelProperty("控制下线_制绒反射率三峰，单位%")
    private Float contrlDownReflexRateThreePeak;

    @ApiModelProperty("规格上线_制绒反射率三峰，单位%")
    private Float standardsUpReflexRateThreePeak;

    @ApiModelProperty("规格下线_制绒反射率三峰，单位%")
    private Float standardsDownReflexRateThreePeak;

    public SpcCz302ZRDetailDTO(DataSpcCz302ZREntity dataSpcCz30202) {
        BeanUtils.copyProperties(dataSpcCz30202, this);
        avgFlex = (NumberUtil.floatValue(dataSpcCz30202.getAvg1()) + NumberUtil.floatValue(dataSpcCz30202.getAvg2())) / 200;
        // (（称重_制绒后1-称重_制绒前1）+（称重_制绒后2-称重_制绒前2）)/2
        avgWeight =
                (
                        (NumberUtil.floatValue(dataSpcCz30202.getWeightThrowBefore1()) - NumberUtil.floatValue(dataSpcCz30202.getWeightThrowAfter1()))
                                + (NumberUtil.floatValue(dataSpcCz30202.getWeightThrowBefore2()) - NumberUtil.floatValue(dataSpcCz30202.getWeightThrowAfter2()))
                ) / 2;
    }

}
