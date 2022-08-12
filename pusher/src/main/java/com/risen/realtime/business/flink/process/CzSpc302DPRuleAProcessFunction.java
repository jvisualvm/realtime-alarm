package com.risen.realtime.business.flink.process;

import com.risen.helper.constant.Symbol;
import com.risen.helper.util.NumberUtil;
import com.risen.realtime.business.flink.constant.SideOutputConstant;
import com.risen.realtime.business.flink.service.FlinkCommonService;
import com.risen.realtime.framework.base.SpcProcessService;
import com.risen.realtime.framework.cosntant.RuleKey;
import com.risen.realtime.framework.service.DingDingService;
import com.risen.realtime.resource.dto.SpcCz302DPDetailDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 20:04
 */
public class CzSpc302DPRuleAProcessFunction extends ProcessFunction<SpcCz302DPDetailDTO, SpcCz302DPDetailDTO> implements SpcProcessService {

    private static final String FLEX_TITLE = "碱抛–反射率";
    private static final String WEIGHT_TITLE = "碱抛–减重";

    @Override
    public void processElement(SpcCz302DPDetailDTO value, ProcessFunction<SpcCz302DPDetailDTO, SpcCz302DPDetailDTO>.Context ctx, Collector<SpcCz302DPDetailDTO> out) throws Exception {
        if (skipThisNode()) {
            out.collect(value);
            return;
        }
        boolean canRun = DingDingService.spcValid(RuleKey.RULE_A);
        if (!canRun|| StringUtils.isEmpty(value.getProcessLine())) {
            return;
        }
        OutputTag<SpcCz302DPDetailDTO> sideOutputAlarmTag = new OutputTag<SpcCz302DPDetailDTO>(SideOutputConstant.SIDE_STREAM) {
        };

        // 规则 A:	A, 任何一点超出+/-3倍标准差区域(上下控制限).
        //当平均减重（x ̅）、平均反射率x ̅、Cpk这三个指标的值符合预警规则时候，系统发起钉钉预警。
        //制绒–减重/反射率/Cpk
        //平均减重
        Float avgWeight = NumberUtil.floatValue(value.getAvgWeight());
        //控制上限_减重
        Float controlUpWeight = NumberUtil.floatValue(value.getControlUpWeight());
        //控制下限_减重
        Float controlDownWeight = NumberUtil.floatValue(value.getControlDownWeight());
        boolean avgWeightControl = FlinkCommonService.spcProcessElementRuleA(value, avgWeight, controlUpWeight, controlDownWeight, WEIGHT_TITLE, sideOutputAlarmTag, ctx, (t) -> {
            //保留3位小数
            return NumberUtil.holdFloat(t, 3, true);
        });


        //平均反射率
        Float avgFlex = NumberUtil.floatValue(value.getAvgFlex());
        //控制上限_反射率
        Float contrlUpReflexRate = NumberUtil.floatValue(value.getContrlUpReflexRate());
        //控制下限_反射率
        Float contrlDownReflexRate = NumberUtil.floatValue(value.getContrlDownReflexRate());
        boolean avgFlexControl = FlinkCommonService.spcProcessElementRuleA(value, avgFlex, contrlUpReflexRate, contrlDownReflexRate, FLEX_TITLE, sideOutputAlarmTag, ctx, (t) -> {
            return NumberUtil.holdFloat(t * 100, 2, true) + Symbol.SYMBOL_BFH;
        });

        //如果上面有触发的，就不需要再继续往下传递数据
        if (!avgWeightControl && !avgFlexControl) {
            out.collect(value);
        }
    }

    @Override
    public boolean skipThisNode() {
        return false;
    }
}
