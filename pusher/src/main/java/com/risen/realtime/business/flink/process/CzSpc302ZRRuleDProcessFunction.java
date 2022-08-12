package com.risen.realtime.business.flink.process;

import com.risen.helper.constant.Symbol;
import com.risen.helper.util.LimitLinkedList;
import com.risen.helper.util.NumberUtil;
import com.risen.realtime.business.flink.constant.SideOutputConstant;
import com.risen.realtime.business.flink.service.FlinkCommonService;
import com.risen.realtime.framework.base.SpcProcessService;
import com.risen.realtime.framework.cache.PushRuleCache;
import com.risen.realtime.framework.cosntant.RuleKey;
import com.risen.realtime.framework.dto.DataAroundDTO;
import com.risen.realtime.framework.entity.PushRuleEntity;
import com.risen.realtime.framework.service.DingDingService;
import com.risen.realtime.framework.service.SystemCacheService;
import com.risen.realtime.framework.service.WindowService;
import com.risen.realtime.resource.dto.SpcCz302ZRDetailDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/4 14:08
 */
public class CzSpc302ZRRuleDProcessFunction extends ProcessFunction<SpcCz302ZRDetailDTO, SpcCz302ZRDetailDTO> implements SpcProcessService {

    private static final String FLEX_TITLE = "制绒–反射率";
    private static final String WEIGHT_TITLE = "制绒–减重";


    private static final String REDIS_FLEX_TYPE_KEY = "FLEX";
    private static final String REDIS_WEIGHT_TYPE_KEY = "WEIGHT";

    /**
     *  触发规则：	B, 连续的7点出现在中心线的同一侧
     *
     * @param value
     * @param ctx
     * @param out
     * @throws Exception
     */
    @Override
    public void processElement(SpcCz302ZRDetailDTO value, ProcessFunction<SpcCz302ZRDetailDTO, SpcCz302ZRDetailDTO>.Context ctx, Collector<SpcCz302ZRDetailDTO> out) throws Exception {
        if (skipThisNode()) {
            out.collect(value);
            return;
        }
        boolean canRun = DingDingService.spcValid(RuleKey.RULE_D);
        //因为是按照线别判断的，线别为空不执行
        if (!canRun || StringUtils.isEmpty(value.getProcessLine())) {
            return;
        }

        OutputTag<SpcCz302ZRDetailDTO> sideOutputAlarmTag = new OutputTag<SpcCz302ZRDetailDTO>(SideOutputConstant.SIDE_STREAM) {
        };

        PushRuleCache pushRuleCache = SystemCacheService.getSystemObj(PushRuleCache.class);
        PushRuleEntity ruleEntity = pushRuleCache.get(RuleKey.RULE_D);
        //首次进入需要初始化窗口数组
        Integer ruleValue = ruleEntity.getRuleValueInt();
        String windowKey = value.getProcessLine().replace("号", "") + value.getTankNumber();
        //初始化窗口
        WindowService windowService = SystemCacheService.getSystemObj(WindowService.class);
        windowService.initDataWindow(value.getTaskKey(), RuleKey.RULE_D, windowKey, REDIS_FLEX_TYPE_KEY, ruleValue);
        //获取窗口
        LimitLinkedList<SpcCz302ZRDetailDTO> flexDataList = windowService.getDataWindow(value.getTaskKey(), RuleKey.RULE_D, windowKey, REDIS_FLEX_TYPE_KEY, ruleValue);

        //先判断是否连续7个点在同一侧
        boolean flexOk = FlinkCommonService.hasOutTagRuleD(value, ruleEntity.getRuleValueInt(), FLEX_TITLE,
                flexDataList,
                () -> {
                    return flexDataList.stream().map(s -> {
                        DataAroundDTO dataAroundDTO = new DataAroundDTO();
                        dataAroundDTO.setSource(s.getAvgFlex());
                        dataAroundDTO.setTarget(s.getMiddleReflexRateThreePeak());
                        return dataAroundDTO;
                    }).collect(Collectors.toList());
                },
                () -> {
                    return flexDataList.stream().map(s -> s.getAvgFlex()).collect(Collectors.toList());
                },
                sideOutputAlarmTag,
                ctx, (data) -> {
                    List<String> result = data.stream().map(s -> {
                        return NumberUtil.holdFloat(s * 100, 2, true) + Symbol.SYMBOL_BFH;
                    }).collect(Collectors.toList());
                    String processLineStr = String.join(Symbol.SYMBOL_COMMA, result);
                    return processLineStr;
                });

        //更新窗口
        windowService.updateDataWindow(value.getTaskKey(), RuleKey.RULE_D, windowKey, REDIS_FLEX_TYPE_KEY, flexDataList);

        //初始化接口
        windowService.initDataWindow(value.getTaskKey(), RuleKey.RULE_D, windowKey, REDIS_WEIGHT_TYPE_KEY, ruleValue);
        //获取窗口
        LimitLinkedList<SpcCz302ZRDetailDTO> weightDataList = windowService.getDataWindow(value.getTaskKey(), RuleKey.RULE_D, windowKey, REDIS_WEIGHT_TYPE_KEY, ruleValue);


        boolean weightOk = FlinkCommonService.hasOutTagRuleD(value, ruleEntity.getRuleValueInt(), WEIGHT_TITLE,
                weightDataList,
                () -> {
                    return weightDataList.stream().map(s -> {
                        DataAroundDTO dataAroundDTO = new DataAroundDTO();
                        dataAroundDTO.setSource(s.getAvgWeight());
                        dataAroundDTO.setTarget(s.getMiddleWeightThreePeak());
                        return dataAroundDTO;
                    }).collect(Collectors.toList());
                },
                () -> {
                    return weightDataList.stream().map(s -> s.getAvgWeight()).collect(Collectors.toList());
                },
                sideOutputAlarmTag,
                ctx, (data) -> {
                    List<String> result = data.stream().map(s -> {
                        //保留3位小数
                        return NumberUtil.holdFloat(s.floatValue(), 3, true);
                    }).collect(Collectors.toList());
                    String processLineStr = String.join(Symbol.SYMBOL_COMMA, result);
                    return processLineStr;
                });

        //更新窗口
        windowService.updateDataWindow(value.getTaskKey(), RuleKey.RULE_D, windowKey, REDIS_WEIGHT_TYPE_KEY, weightDataList);

        if (!weightOk && !flexOk) {
            out.collect(value);
        }
    }

    @Override
    public boolean skipThisNode() {
        return false;
    }
}
