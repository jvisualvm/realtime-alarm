package com.risen.realtime.business.flink.process;

import com.risen.helper.constant.Symbol;
import com.risen.helper.util.NumberUtil;
import com.risen.realtime.business.flink.constant.SideOutputConstant;
import com.risen.realtime.business.flink.service.FlinkCommonService;
import com.risen.realtime.framework.base.SpcProcessService;
import com.risen.realtime.framework.cosntant.RuleKey;
import com.risen.realtime.framework.service.DingDingService;
import com.risen.realtime.resource.dto.SpcCz302ZRDetailDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 20:04
 */
public class CzSpc302ZRRuleAProcessFunction extends ProcessFunction<SpcCz302ZRDetailDTO, SpcCz302ZRDetailDTO> implements SpcProcessService {
    private static final String FLEX_TITLE = "制绒–反射率";
    private static final String WEIGHT_TITLE = "制绒–减重";

    @Override
    public void processElement(SpcCz302ZRDetailDTO value, ProcessFunction<SpcCz302ZRDetailDTO, SpcCz302ZRDetailDTO>.Context ctx, Collector<SpcCz302ZRDetailDTO> out) throws Exception {
        if (skipThisNode()) {
            out.collect(value);
            return;
        }
        boolean canRun = DingDingService.spcValid(RuleKey.RULE_A);
        if (!canRun|| StringUtils.isEmpty(value.getProcessLine())) {
            return;
        }
        OutputTag<SpcCz302ZRDetailDTO> sideOutputAlarmTag = new OutputTag<SpcCz302ZRDetailDTO>(SideOutputConstant.SIDE_STREAM) {
        };

        // 规则 A:	A, 任何一点超出+/-3倍标准差区域(上下控制限).
        //当平均减重（x ̅）、平均反射率x ̅、Cpk这三个指标的值符合预警规则时候，系统发起钉钉预警。
        //制绒–减重/反射率/Cpk
        //平均减重
        Float avgWeight = NumberUtil.floatValue(value.getAvgWeight());
        //控制上限_减重
        Float controlUpWeight = NumberUtil.floatValue(value.getContrlUpWeightThreePeak());
        //控制下限_减重
        Float controlDownWeight = NumberUtil.floatValue(value.getContrlDownWeightThreePeak());
        boolean avgWeightControl = FlinkCommonService.spcProcessElementRuleA(value, avgWeight, controlUpWeight, controlDownWeight, WEIGHT_TITLE, sideOutputAlarmTag, ctx, (t) -> {
            //保留3位小数
            return NumberUtil.holdFloat(t, 3, true);
        });


        //平均反射率
        Float avgFlex = NumberUtil.floatValue(value.getAvgFlex());
        //控制上限_反射率
        Float contrlUpReflexRate = NumberUtil.floatValue(value.getContrlUpReflexRateThreePeak());
        //控制下限_反射率
        Float contrlDownReflexRate = NumberUtil.floatValue(value.getContrlDownReflexRateThreePeak());
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
