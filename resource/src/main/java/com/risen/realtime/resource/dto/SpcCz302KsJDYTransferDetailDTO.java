package com.risen.realtime.resource.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/13 11:11
 */
@Data
public class SpcCz302KsJDYTransferDetailDTO implements Serializable {

    @ApiModelProperty("中心值")
    private Float middleRate;

    @ApiModelProperty("控制下限")
    private Float contrlDownRate;

    @ApiModelProperty("控制上限")
    private Float contrlUpRate;

    public void updateAllField(SpcCz302KsJDYDetailDTO ksJDYDetailDTO) {
        this.middleRate = Float.parseFloat(ksJDYDetailDTO.get_widget_1649926530636()); //中心值
        this.contrlUpRate = Float.parseFloat(ksJDYDetailDTO.get_widget_1649926530442());//控制上限
        this.contrlDownRate = Float.parseFloat(ksJDYDetailDTO.get_widget_1649926530494());//控制下限
    }


}
