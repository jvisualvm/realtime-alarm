package com.risen.realtime.business.flink.service;

import com.risen.helper.constant.Symbol;
import com.risen.helper.util.LimitLinkedList;
import com.risen.realtime.framework.algorithm.DataDistributedAlgorithmable;
import com.risen.realtime.framework.base.SpcBaseDetail;
import com.risen.realtime.framework.cache.PushRuleCache;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.cosntant.RuleKey;
import com.risen.realtime.framework.dto.DataAroundDTO;
import com.risen.realtime.framework.entity.PushRuleEntity;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.event.DingDingPublisher;
import com.risen.realtime.framework.service.SystemCacheService;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.OutputTag;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/3 12:57
 */
public class FlinkCommonService {

    public static boolean overUpDownBound(Float avg, Float up, Float down) {
        return avg < down || avg > up;
    }

    public static <T extends SpcBaseDetail> void spcProccess(T value, Supplier<String> dataTime, Supplier<String> processLine) {
        PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
        PushTaskEntity pushTask = pushTaskCache.get(value.getTaskKey());
        //可能配置需要推送多个地址
        PushRuleCache pushRuleCache = SystemCacheService.getSystemObj(PushRuleCache.class);

        DingDingPublisher dingDingPublisher = SystemCacheService.getSystemObj(DingDingPublisher.class);

        PushRuleEntity ruleEntity = pushRuleCache.get(value.getRule());
        dingDingPublisher.publish(value.getTaskKey(), value, (s) -> {
            return String.format(s, pushTask.getTitle(), ruleEntity.getRuleName(), dataTime.get(), value.getWorkProcedure(), processLine.get(), value.getReason());
        });
    }

    public static String sideOutputAlarmTagMessageRuleA(Float value, Boolean isUp, Function<Float, String> reasonFunction) {
        StringBuilder builder = new StringBuilder();
        builder.append("如下点位异常：").append(reasonFunction.apply(value));
        if (isUp) {
            builder.append(Symbol.SYMBOL_END).append("超出控制上限");
        } else {
            builder.append(Symbol.SYMBOL_END).append("超出控制下限");
        }
        return builder.toString();
    }

    public static <T extends SpcBaseDetail> void sideOutputAlarmTagMessageRuleB(T value, Integer ruleKey, List<Number> reason, Boolean isUp,
                                                                                OutputTag<T> sideOutputAlarmTag,
                                                                                ProcessFunction<T, T>.Context ctx, Function<List<Number>, String> reasonFunction) {
        StringBuilder builder = new StringBuilder();
        String processLineStr = reasonFunction.apply(reason);
        builder.append("如下点位异常：").
                append(processLineStr).
                append(Symbol.SYMBOL_END).
                append("总共").
                append(ruleKey);

        if (isUp) {
            builder.append("点在中心线上侧");
        } else {
            builder.append("点在中心线下侧");
        }

        value.setReason(builder.toString());
        ctx.output(sideOutputAlarmTag, value);
    }


    public static <T extends SpcBaseDetail> void sideOutputAlarmTagMessageRuleC(T value, Integer ruleValue, List<Float> reason,
                                                                                ProcessFunction<T, T>.Context ctx, OutputTag<T> sideOutputAlarmTag, boolean isUp, Function<List<Float>, String> reasonFunction) {

        StringBuilder builder = new StringBuilder();
        String processLineStr = reasonFunction.apply(reason);
        //连续的9点呈现上升或下降趋势
        builder.append("如下点位异常：").
                append(processLineStr).
                append(Symbol.SYMBOL_END).
                append("连续的").
                append(ruleValue + "点呈现");
        if (isUp) {
            builder.append("上升趋势");
        } else {
            builder.append("下降趋势");
        }
        value.setReason(builder.toString());
        ctx.output(sideOutputAlarmTag, value);
    }


    public static <T extends SpcBaseDetail> void sideOutputAlarmTagMessageRuleD(T value, Integer ruleValue,
                                                                                ProcessFunction<T, T>.Context ctx,
                                                                                OutputTag<T> sideOutputAlarmTag, List<Float> reason, Function<List<Float>, String> reasonFunction) {
        StringBuilder builder = new StringBuilder();
        String processLineStr = reasonFunction.apply(reason);
        builder.append("如下点位异常：").
                append(processLineStr).
                append(Symbol.SYMBOL_END).
                append("连续的").
                append(ruleValue + "点交替出现在中心线的两侧");
        value.setReason(builder.toString());
        ctx.output(sideOutputAlarmTag, value);
    }


    public static <T extends SpcBaseDetail> boolean spcProcessElementRuleA(T value, Float avg, Float contrlUpValue, Float contrlDownValue, String title, OutputTag<T> sideOutputAlarmTag, ProcessFunction<T, T>.Context ctx, Function<Float, String> reasonFunction) {
        boolean avgFlexControl = FlinkCommonService.overUpDownBound(avg, contrlUpValue, contrlDownValue);
        if (avgFlexControl) {
            boolean isUp = avg > contrlUpValue;
            value.updateKeyDetailNotTask(RuleKey.RULE_A, title, FlinkCommonService.sideOutputAlarmTagMessageRuleA(avg, isUp, reasonFunction));
            ctx.output(sideOutputAlarmTag, value);
            return true;
        }
        return false;
    }


    public static <T extends SpcBaseDetail> boolean hasRuleBOutTag(T value, Integer ruleValue,
                                                                   LimitLinkedList<T> dataList,
                                                                   String title,
                                                                   Supplier<Long> upValue,
                                                                   Supplier<Long> downValue,
                                                                   Supplier<List<Number>> reasonValueList,
                                                                   OutputTag<T> sideOutputAlarmTag,
                                                                   ProcessFunction<T, T>.Context ctx,
                                                                   Function<List<Number>, String> reasonFunction) {
        value.setRule(RuleKey.RULE_B);
        value.setWorkProcedure(title);
        if (dataList.size() < ruleValue) {
            dataList.addNotRemoveHead(value);
        } else {
            //执行到这里，说明窗口已经N个值了
            long flexUpCount = upValue.get();
            long flexDownCount = downValue.get();
            //满足条件，需要输出侧输出流
            if (flexUpCount == ruleValue || flexDownCount == ruleValue) {
                boolean isUp = flexUpCount == ruleValue;

                //当平均减重（x ̅）、平均反射率x ̅、Cpk
                FlinkCommonService.sideOutputAlarmTagMessageRuleB(value, ruleValue, reasonValueList.get(), isUp,
                        sideOutputAlarmTag, ctx, reasonFunction);
                //发送完毕，清空数据
                dataList.clear();
                return true;
            }
            dataList.addRemoveHead(value);
        }
        return false;
    }


    public static <T extends SpcBaseDetail> boolean hasRuleCOutTag(T value, Integer ruleValue, String title, LimitLinkedList<T> dataList,
                                                                   Supplier<List<Float>> dataAgainValue,
                                                                   Supplier<List<Float>> outPutValue,
                                                                   OutputTag<T> sideOutputAlarmTag,
                                                                   ProcessFunction<T, T>.Context ctx, Function<List<Float>, String> reasonFunction) {
        AtomicBoolean isExists = new AtomicBoolean(false);
        value.setWorkProcedure(title);
        value.setRule(RuleKey.RULE_C);
        if (dataList.size() < ruleValue) {
            dataList.addNotRemoveHead(value);
        } else {
            Map<Boolean, Boolean> result = DataDistributedAlgorithmable.ascDescAgain(dataAgainValue.get());
            ascDescAgain(result, ruleValue, value, ctx, dataList, outPutValue.get(), sideOutputAlarmTag, reasonFunction);
        }
        return isExists.get();
    }


    private static <T extends SpcBaseDetail> boolean ascDescAgain(Map<Boolean, Boolean> result, Integer ruleValue, T value,
                                                                  ProcessFunction<T, T>.Context ctx,
                                                                  LimitLinkedList<T> lst, List<Float> reason, OutputTag<T> sideOutputAlarmTag, Function<List<Float>, String> reasonFunction) {
        AtomicBoolean isExists = new AtomicBoolean(false);
        result.forEach((k, v) -> {
            //递增
            if (k && v) {
                //当平均减重（x ̅）、平均反射率x ̅、Cpk
                FlinkCommonService.sideOutputAlarmTagMessageRuleC(value, ruleValue, reason,

                        ctx, sideOutputAlarmTag, true, reasonFunction);
                isExists.set(true);
                //发送完毕，清空数据
                lst.clear();
            }
            //递减
            if (k && !v) {
                FlinkCommonService.sideOutputAlarmTagMessageRuleC(value, ruleValue, reason,
                        ctx, sideOutputAlarmTag, false, reasonFunction);
                isExists.set(true);
                //发送完毕，清空数据
                lst.clear();
            }
        });
        lst.addRemoveHead(value);
        return isExists.get();
    }


    public static <T extends SpcBaseDetail> boolean hasOutTagRuleD(T value, Integer ruleValue, String title,
                                                                   LimitLinkedList<T> dataList,
                                                                   Supplier<List<DataAroundDTO>> mathDataList,
                                                                   Supplier<List<Float>> outPutValueList,
                                                                   OutputTag<T> sideOutputAlarmTag,
                                                                   ProcessFunction<T, T>.Context ctx, Function<List<Float>, String> reasonFunction) {
        AtomicBoolean isExists = new AtomicBoolean(false);
        value.setRule(RuleKey.RULE_D);
        if (dataList.size() < ruleValue) {
            dataList.addNotRemoveHead(value);
        } else {
            value.setWorkProcedure(title);
            boolean isMatch = DataDistributedAlgorithmable.dataAround(mathDataList.get());
            if (isMatch) {
                FlinkCommonService.sideOutputAlarmTagMessageRuleD(value, ruleValue,
                        ctx,
                        sideOutputAlarmTag, outPutValueList.get(), reasonFunction);
                //发送完毕，清空数据
                dataList.clear();
            }
            dataList.addRemoveHead(value);
        }
        return isExists.get();
    }

}
