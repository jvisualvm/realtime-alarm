package com.risen.realtime.resource.dto;

import com.risen.helper.util.NumberUtil;
import com.risen.realtime.framework.base.SpcBaseDetail;
import com.risen.realtime.resource.report.zeus.entity.DataSpcCz302BDHEntity;
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
public class SpcCz302BDHDetailDTO extends SpcBaseDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    public SpcCz302BDHDetailDTO() {
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

    private String luGuan;

    public SpcCz302BDHDetailDTO(DataSpcCz302BDHEntity bdhEntity) {
        BeanUtils.copyProperties(bdhEntity, this);
        processLine = bdhEntity.getXianBie();
        this.avg = NumberUtil.floatValue(bdhEntity.getAverage());
        this.middleRate = NumberUtil.floatValue(bdhEntity.getCenterValue());
        this.contrlUpRate = NumberUtil.floatValue(bdhEntity.getControlUpperLimit());
        this.contrlDownRate = NumberUtil.floatValue(bdhEntity.getControlLowerLimit());
    }

}
