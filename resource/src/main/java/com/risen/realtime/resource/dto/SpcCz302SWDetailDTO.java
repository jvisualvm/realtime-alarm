package com.risen.realtime.resource.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.risen.helper.util.NumberUtil;
import com.risen.realtime.framework.base.SpcBaseDetail;
import com.risen.realtime.resource.report.analysis.entity.DataSpcCz302SWEntity;
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
public class SpcCz302SWDetailDTO extends SpcBaseDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    public SpcCz302SWDetailDTO() {
    }

    @ApiModelProperty("线别")
    private String processLine;

    @ApiModelProperty("日期")
    private String dataTime;

    @ApiModelProperty("平均值")
    private Float avg;

    @ApiModelProperty("中心值")
    private Float middle;

    @ApiModelProperty("控制上限")
    private Float conUp;

    @ApiModelProperty("控制下限")
    private Float conLow;

    @ApiModelProperty("班次，N:夜班(19:30<=T<次日7:30)，D:白班(7:30<=T<19:30)")
    private String ban;

    @ApiModelProperty("13:30（1:30）湿重（增重量）")
    private String time1;

    @ApiModelProperty("15:30（3:30）湿重（增重量）")
    private String time3;

    @ApiModelProperty("17:30（5:30）湿重（增重量）")
    @TableField("time5")
    private String time5;

    @ApiModelProperty("9:30（21:30）湿重（增重量）")
    private String time9;

    @ApiModelProperty("11:30（23:30）湿重（增重量）")
    private String time11;

    private String dao;


    public SpcCz302SWDetailDTO(DataSpcCz302SWEntity swEntity) {
        BeanUtils.copyProperties(swEntity, this);
        this.processLine = swEntity.getLine();
        this.middle = NumberUtil.floatValue(swEntity.getTarget());
        this.conUp = NumberUtil.floatValue(swEntity.getConUp());
        this.conLow = NumberUtil.floatValue(swEntity.getConLow());
    }


}