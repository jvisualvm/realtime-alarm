package com.risen.realtime.business.flink.process;

import com.risen.helper.util.NumberUtil;
import com.risen.realtime.business.flink.constant.SideOutputConstant;
import com.risen.realtime.business.flink.service.FlinkCommonService;
import com.risen.realtime.framework.base.SpcProcessService;
import com.risen.realtime.framework.cosntant.RuleKey;
import com.risen.realtime.framework.service.DingDingService;
import com.risen.realtime.resource.dto.SpcCz302BDHDetailDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 20:04
 */
public class CzSpc302BDHRuleAProcessFunction extends ProcessFunction<SpcCz302BDHDetailDTO, SpcCz302BDHDetailDTO> implements SpcProcessService {

    private static final String FZ_AVG__TITLE = "背钝化–膜厚";

    @Override
    public void processElement(SpcCz302BDHDetailDTO value, ProcessFunction<SpcCz302BDHDetailDTO, SpcCz302BDHDetailDTO>.Context ctx, Collector<SpcCz302BDHDetailDTO> out) throws Exception {

        if (skipThisNode()) {
            out.collect(value);
            return;
        }

        boolean canRun = DingDingService.spcValid(RuleKey.RULE_A);
        if (!canRun || StringUtils.isEmpty(value.getProcessLine())) {
            return;
        }

        OutputTag<SpcCz302BDHDetailDTO> sideOutputAlarmTag = new OutputTag<SpcCz302BDHDetailDTO>(SideOutputConstant.SIDE_STREAM) {
        };

        Float avg = NumberUtil.floatValue(value.getAvg());
        Float controlUp = NumberUtil.floatValue(value.getContrlUpRate());
        Float controlDown = NumberUtil.floatValue(value.getContrlDownRate());
        boolean isContinue = FlinkCommonService.spcProcessElementRuleA(value, avg, controlUp, controlDown, FZ_AVG__TITLE, sideOutputAlarmTag, ctx, (t) -> {
            //保留3位小数
            return NumberUtil.holdFloat(t, 3, true);
        });

        //如果上面有触发的，就不需要再继续往下传递数据
        if (!isContinue) {
            out.collect(value);
        }
    }


    @Override
    public boolean skipThisNode() {
        return false;
    }
}
