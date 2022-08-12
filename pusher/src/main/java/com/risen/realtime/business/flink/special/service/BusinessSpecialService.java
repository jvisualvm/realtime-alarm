package com.risen.realtime.business.flink.special.service;

import com.risen.helper.consumer.ConditionConsumer;
import com.risen.helper.util.DateUtil;
import com.risen.helper.util.IfUtil;
import com.risen.helper.util.ObjectsUtil;
import com.risen.realtime.business.flink.special.constant.ClassOrderEnum;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/8 14:12
 * @Descritption 个性化service
 */
@Service
public class BusinessSpecialService {

    public static String getDataTimeByFunction(String dataTime, String classOrder, Function<Boolean, String> appendTime) {
        AtomicReference<String> finalTime = new AtomicReference<>();
        ConditionConsumer ifConsumer = () -> {
            finalTime.set(DateUtil.appendTime(dataTime, appendTime.apply(true)));
        };
        ConditionConsumer elseConsumer = () -> {
            finalTime.set(DateUtil.appendTime(dataTime, appendTime.apply(false)));
        };
        IfUtil.goIf(ifConsumer, elseConsumer, ClassOrderEnum.WHITE.getCode().equals(classOrder));
        return finalTime.get();
    }

    public static String getClassOrderDataTimeByTime(String dataTime, String classOrder, Object... obs) {
        AtomicReference<String> finalTime = new AtomicReference<>();
        final String time = ObjectsUtil.findNotEmpty(obs).toString();
        ConditionConsumer ifConsumer = () -> {
            finalTime.set(DateUtil.appendTime(dataTime, time));
        };
        ConditionConsumer elseConsumer = () -> {
            finalTime.set(DateUtil.appendTime(dataTime, time));
        };
        IfUtil.goIf(ifConsumer, elseConsumer, ClassOrderEnum.WHITE.getCode().equals(classOrder));
        return finalTime.get();
    }
}
