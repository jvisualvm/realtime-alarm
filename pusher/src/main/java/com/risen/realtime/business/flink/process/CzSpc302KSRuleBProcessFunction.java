package com.risen.realtime.business.flink.process;

import com.risen.helper.constant.Symbol;
import com.risen.helper.util.LimitLinkedList;
import com.risen.helper.util.NumberUtil;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.business.flink.constant.SideOutputConstant;
import com.risen.realtime.business.flink.service.FlinkCommonService;
import com.risen.realtime.framework.base.SpcProcessService;
import com.risen.realtime.framework.cache.PushRuleCache;
import com.risen.realtime.framework.cosntant.RuleKey;
import com.risen.realtime.framework.entity.PushRuleEntity;
import com.risen.realtime.framework.service.DingDingService;
import com.risen.realtime.framework.service.SystemCacheService;
import com.risen.realtime.framework.service.WindowService;
import com.risen.realtime.resource.dto.SpcCz302KSDetailDTO;
import com.risen.realtime.resource.dto.SpcCz302SWDetailDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/4 14:08
 */
public class CzSpc302KSRuleBProcessFunction extends ProcessFunction<SpcCz302KSDetailDTO, SpcCz302KSDetailDTO> implements SpcProcessService {

    private static final String FZ_AVG__TITLE = "扩散–%s方阻";

    private static final String REDIS_FLEX_TYPE_KEY = "AVG";

    /**
     *  触发规则：	B, 连续的7点出现在中心线的同一侧
     *
     * @param value
     * @param ctx
     * @param out
     * @throws Exception
     */
    @Override
    public void processElement(SpcCz302KSDetailDTO value, ProcessFunction<SpcCz302KSDetailDTO, SpcCz302KSDetailDTO>.Context ctx, Collector<SpcCz302KSDetailDTO> out) throws Exception {
        if (skipThisNode()) {
            out.collect(value);
            return;
        }
        boolean canRun = DingDingService.spcValid(RuleKey.RULE_B);
        //因为是按照线别判断的，线别为空不执行
        if (!canRun || StringUtils.isEmpty(value.getProcessLine())) {
            return;
        }
        OutputTag<SpcCz302KSDetailDTO> sideOutputAlarmTag = new OutputTag<SpcCz302KSDetailDTO>(SideOutputConstant.SIDE_STREAM) {
        };
        PushRuleCache pushRuleCache = SystemCacheService.getSystemObj(PushRuleCache.class);
        PushRuleEntity ruleEntity = pushRuleCache.get(RuleKey.RULE_B);
        //首次进入需要初始化窗口数组
        Integer ruleValue = ruleEntity.getRuleValueInt();
        String windowKey = value.getProcessLine().replace("号", "")+ value.getIotId()+value.getWenQu();
        //初始化窗口
        WindowService windowService = SystemCacheService.getSystemObj(WindowService.class);
        windowService.initDataWindow(value.getTaskKey(), RuleKey.RULE_B, windowKey, REDIS_FLEX_TYPE_KEY, ruleValue);
        //获取窗口
        LimitLinkedList<SpcCz302KSDetailDTO> flexDataList = windowService.getDataWindow(value.getTaskKey(), RuleKey.RULE_B, windowKey, REDIS_FLEX_TYPE_KEY, ruleValue);

        SpcCz302KSDetailDTO ksDetailDTO = null;
        if (!CollectionUtils.isEmpty(flexDataList)) {
            ksDetailDTO = flexDataList.get(0);
        }

        boolean weightOk = FlinkCommonService.hasRuleBOutTag(value, ruleValue,
                flexDataList,
                String.format(FZ_AVG__TITLE, PredicateUtil.isNotEmpty(ksDetailDTO)?ksDetailDTO.getOnline():value.getOnline()),
                () -> {
                    return flexDataList.stream().filter(s -> s.getAvg() > s.getMiddleRate()).count();
                },
                () -> {
                    return flexDataList.stream().filter(s -> s.getAvg() < s.getMiddleRate()).count();
                },
                () -> {
                    return flexDataList.stream().map(s -> s.getAvg()).collect(Collectors.toList());
                }, sideOutputAlarmTag,
                ctx, (data) -> {
                    List<String> result = data.stream().map(s -> {
                        //保留3位小数
                        return NumberUtil.holdFloat(s.floatValue(), 3, true);
                    }).collect(Collectors.toList());
                    String processLineStr = String.join(Symbol.SYMBOL_COMMA, result);
                    return processLineStr;
                });
        //更新窗口
        windowService.updateDataWindow(value.getTaskKey(), RuleKey.RULE_B, windowKey, REDIS_FLEX_TYPE_KEY, flexDataList);

        if (!weightOk) {
            out.collect(value);
        }
    }



    @Override
    public boolean skipThisNode() {
        return false;
    }

}
