package com.risen.realtime.resource.dto;

import com.risen.helper.util.NumberUtil;
import com.risen.realtime.framework.base.SpcBaseDetail;
import com.risen.realtime.resource.report.analysis.entity.DataSpcCz302DPEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import scala.Serializable;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 10:19
 */
@Data
public class SpcCz302DPDetailDTO extends SpcBaseDetail implements Serializable {
    public SpcCz302DPDetailDTO() {

    }

    private Long dataKey;

    @ApiModelProperty("线别")
    private String processLine;

    @ApiModelProperty("时间，yyyy-mm-dd hh24:mi:ss")
    private String dataTime;

    @ApiModelProperty("平均减重")
    private Float avgWeight;

    @ApiModelProperty("平均反射率")
    private Float avgFlex;

    @ApiModelProperty("中心值_减重")
    private Float middleReduceWeight;

    @ApiModelProperty("控制下限_减重")
    private Float controlDownWeight;

    @ApiModelProperty("控制上限_减重")
    private Float controlUpWeight;

    @ApiModelProperty("中心值_反射率")
    private Float middleReflexRate;

    @ApiModelProperty("控制下限_反射率")
    private Float contrlDownReflexRate;

    @ApiModelProperty("控制上限_反射率")
    private Float contrlUpReflexRate;

    @ApiModelProperty("碱抛槽体号")
    private String tankNumber;

    public SpcCz302DPDetailDTO(DataSpcCz302DPEntity dataSpcCz30207) {
        BeanUtils.copyProperties(dataSpcCz30207, this);
        avgFlex = (NumberUtil.floatValue(dataSpcCz30207.getAvg1()) + NumberUtil.floatValue(dataSpcCz30207.getAvg2())) / 200;
        // (（称重_制绒后1-称重_制绒前1）+（称重_制绒后2-称重_制绒前2）)/2
        avgWeight =
                (
                        (NumberUtil.floatValue(dataSpcCz30207.getWeightThrowBefore1()) - NumberUtil.floatValue(dataSpcCz30207.getWeightThrowAfter1()))
                                + (NumberUtil.floatValue(dataSpcCz30207.getWeightThrowBefore2()) - NumberUtil.floatValue(dataSpcCz30207.getWeightThrowAfter2()))
                ) / 2;
    }
}
