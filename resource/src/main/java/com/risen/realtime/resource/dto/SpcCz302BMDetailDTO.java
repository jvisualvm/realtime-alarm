package com.risen.realtime.resource.dto;

import com.risen.helper.util.NumberUtil;
import com.risen.realtime.framework.base.SpcBaseDetail;
import com.risen.realtime.resource.report.zeus.entity.DataSpcCz302BMEntity;
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
public class SpcCz302BMDetailDTO extends SpcBaseDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    public SpcCz302BMDetailDTO() {
    }

    @ApiModelProperty("日期，yyyy-mm-dd hh24:mi")
    private String dataTime;

    @ApiModelProperty("线别")
    private String processLine;

    private Float avg;

    @ApiModelProperty("中心值")
    private Float middleRate;

    @ApiModelProperty("控制下限")
    private Float contrlDownRate;

    @ApiModelProperty("控制上限")
    private Float contrlUpRate;

    @ApiModelProperty("炉管号")
    private String luGuan;

    public SpcCz302BMDetailDTO(DataSpcCz302BMEntity bmEntity) {
        BeanUtils.copyProperties(bmEntity, this);
        this.avg = NumberUtil.floatValue(bmEntity.getAverage());
        this.processLine = bmEntity.getXianBie();
        this.middleRate = NumberUtil.floatValue(bmEntity.getCenterValue());
        this.contrlUpRate = NumberUtil.floatValue(bmEntity.getControlUpperLimit());
        this.contrlDownRate = NumberUtil.floatValue(bmEntity.getControlLowerLimit());
    }

}
