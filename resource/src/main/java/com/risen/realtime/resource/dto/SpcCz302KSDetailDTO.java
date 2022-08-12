package com.risen.realtime.resource.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.risen.helper.constant.Symbol;
import com.risen.helper.util.NumberUtil;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.framework.base.SpcBaseDetail;
import com.risen.realtime.resource.report.analysis.entity.DataSpcCz302KSEntity;
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
public class SpcCz302KSDetailDTO extends SpcBaseDetail implements Serializable {

    public SpcCz302KSDetailDTO() {
    }

    private static final long serialVersionUID = 1L;

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

    private String online;

    private String iotId;

    @ApiModelProperty("温区")
    private String wenQu;

    public SpcCz302KSDetailDTO(DataSpcCz302KSEntity dataSpcCz30207) {
        BeanUtils.copyProperties(dataSpcCz30207, this);
        this.avg = NumberUtil.floatValue(dataSpcCz30207.getAverageNum());
        if (PredicateUtil.isNotEmpty(dataSpcCz30207.getIotId())) {
            this.processLine = dataSpcCz30207.getIotId().split(Symbol.SYMBOL_LOW)[0];
        }
    }

}
