package com.risen.realtime.resource.dto;

import com.risen.helper.util.NumberUtil;
import com.risen.realtime.framework.base.SpcBaseDetail;
import com.risen.realtime.resource.report.analysis.entity.DataSpcCz302ZMEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

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
public class SpcCz302ZMDetailDTO extends SpcBaseDetail implements Serializable {

    public SpcCz302ZMDetailDTO() {

    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("日期，yyyy-mm-dd")
    private String dataTime;

    @ApiModelProperty("线别")
    private String processLine;

    @ApiModelProperty("平均膜厚")
    private Float avgThickness;

    @ApiModelProperty("平均反射率")
    private Float avgFlex;

    @ApiModelProperty("中心值_膜厚")
    private Float middleThickness;

    @ApiModelProperty("控制下限_膜厚")
    private Float controlDownThickness;

    @ApiModelProperty("控制上限_膜厚")
    private Float controlUpThickness;

    @ApiModelProperty("中心值_反射")
    private Float middleReflexRate;

    @ApiModelProperty("控制下限_反射")
    private Float contrlDownReflexRate;

    @ApiModelProperty("控制上限_反射")
    private Float contrlUpReflexRate;

    @ApiModelProperty("炉管号")
    private String iotId;


    public SpcCz302ZMDetailDTO(DataSpcCz302ZMEntity dataSpcCz3021001) {
        BeanUtils.copyProperties(dataSpcCz3021001, this);

        //TODO 等待提供宽表
        //平均膜厚
        this.avgFlex = NumberUtil.floatValue(dataSpcCz3021001.getRefractivity());
        //平均反射率
        this.avgThickness = NumberUtil.floatValue(dataSpcCz3021001.getFilmThickness());
        this.processLine = dataSpcCz3021001.getLine();
        this.middleThickness = NumberUtil.floatValue(dataSpcCz3021001.getMhYhCn());
        this.controlDownThickness = NumberUtil.floatValue(dataSpcCz3021001.getMhYhB());
        this.controlUpThickness = NumberUtil.floatValue(dataSpcCz3021001.getMhYhA());
        this.middleReflexRate = NumberUtil.floatValue(dataSpcCz3021001.getZsYhCn());
        this.contrlDownReflexRate = NumberUtil.floatValue(dataSpcCz3021001.getZsYhB());
        this.contrlUpReflexRate = NumberUtil.floatValue(dataSpcCz3021001.getZsYhA());
        this.dataTime = dataSpcCz3021001.getDataTime();
    }


}
