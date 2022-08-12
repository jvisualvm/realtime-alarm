package com.risen.realtime.business.flink.process.alarm;

import com.risen.helper.constant.Symbol;
import com.risen.realtime.business.flink.constant.WorkShopConstant;
import com.risen.realtime.business.flink.service.FlinkCommonService;
import com.risen.realtime.resource.dto.SpcCz302ZMDetailDTO;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 20:04
 */
public class CzSpc302ZMAlarmProcessFunction<T extends SpcCz302ZMDetailDTO> extends ProcessFunction<T, T> {

    /**
     * 组装消息然后推送钉钉
     *
     * @param ctx
     * @param out
     * @throws Exception
     */
    @Override
    public void processElement(T value, ProcessFunction<T, T>.Context ctx, Collector<T> out) throws Exception {

        FlinkCommonService.spcProccess(value, () -> {
            return value.getDataTime();
        }, () -> {
            StringBuilder builder = new StringBuilder();
            builder.append(value.getProcessLine().replace(WorkShopConstant.PROCESSLINE_SUFFIX, Symbol.EMPTY_STR));
            builder.append(Symbol.SYMBOL_LOW);
            builder.append(value.getIotId());
            builder.append(WorkShopConstant.PROCESSLINE_SUFFIX);
            return builder.toString();
        });
    }

}
