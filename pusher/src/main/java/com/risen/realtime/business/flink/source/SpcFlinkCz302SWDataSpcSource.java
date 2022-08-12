package com.risen.realtime.business.flink.source;

import com.risen.helper.util.DateUtil;
import com.risen.helper.util.NumberUtil;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.business.flink.cache.FlinkBeanCache;
import com.risen.realtime.business.flink.constant.WorkShopConstant;
import com.risen.realtime.business.flink.special.service.BusinessSpecialService;
import com.risen.realtime.framework.base.SpcSourceService;
import com.risen.realtime.framework.service.DataSourceBaseService;
import com.risen.realtime.framework.service.SystemCacheService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302SWDetailDTO;
import com.risen.realtime.resource.report.analysis.mapper.DataSpcCz302SWMapper;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 17:27
 */
public class SpcFlinkCz302SWDataSpcSource extends RichSourceFunction<SpcCz302SWDetailDTO> implements SpcSourceService {

    @Override
    public void run(SourceContext ctx) {
        FlinkBeanCache flinkBeanCache = SystemCacheService.getSystemObj(FlinkBeanCache.class);
        DataSpcCz302SWMapper dataSpcCz302SWMapper =(DataSpcCz302SWMapper)flinkBeanCache.getObj(DataSpcCz302SWMapper.class);
        DataSourceBaseService.buildDataSource(taskKey(), 5000, dataSpcCz302SWMapper, (t) -> {
            SpcCz302SWDetailDTO cz302ZRDetailDTO = new SpcCz302SWDetailDTO(t);
            try {
                Float avg = buildDataTime(cz302ZRDetailDTO);
                cz302ZRDetailDTO.setAvg(avg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cz302ZRDetailDTO.setTaskKey(taskKey());
            if (PredicateUtil.isNotEmpty(cz302ZRDetailDTO.getProcessLine())) {
                StringBuilder builder = new StringBuilder();
                builder.append(cz302ZRDetailDTO.getProcessLine());
                builder.append(WorkShopConstant.PROCESSLINE_SUFFIX);
                cz302ZRDetailDTO.setProcessLine(builder.toString());
            }
            return cz302ZRDetailDTO;
        }, (t) -> {
            return true;
        }, (t) -> {
            return true;
        }, (maxKey) -> {
            return maxKey.stream().mapToLong(l -> l.getDataKey()).max().getAsLong();
        }, ctx);
    }


    private Float buildDataTime(SpcCz302SWDetailDTO cz302ZRDetailDTO) {
        AtomicReference<Float> weight = new AtomicReference<>(null);
        //日期填充
        String time = null;
        try {
            time = BusinessSpecialService.getDataTimeByFunction(cz302ZRDetailDTO.getDataTime(), cz302ZRDetailDTO.getBan(), (white) -> {
                if (white) {
                    if (PredicateUtil.isNotEmpty(cz302ZRDetailDTO.getTime1())) {
                        weight.set(Float.parseFloat(cz302ZRDetailDTO.getTime1()));
                        return "13:30:00";
                    } else if (PredicateUtil.isNotEmpty(cz302ZRDetailDTO.getTime3())) {
                        weight.set(Float.parseFloat(cz302ZRDetailDTO.getTime3()));
                        return "15:30:00";
                    } else if (PredicateUtil.isNotEmpty(cz302ZRDetailDTO.getTime5())) {
                        weight.set(Float.parseFloat(cz302ZRDetailDTO.getTime5()));
                        return "17:30:00";
                    } else if (PredicateUtil.isNotEmpty(cz302ZRDetailDTO.getTime9())) {
                        weight.set(Float.parseFloat(cz302ZRDetailDTO.getTime9()));
                        return "9:30:00";
                    } else if (PredicateUtil.isNotEmpty(cz302ZRDetailDTO.getTime11())) {
                        weight.set(Float.parseFloat(cz302ZRDetailDTO.getTime11()));
                        return "11:30:00";
                    } else {
                        return DateUtil.parseDate(System.currentTimeMillis(), DateUtil.YYYY_MM_DD_HH_MM_SS);
                    }
                } else {
                    if (PredicateUtil.isNotEmpty(cz302ZRDetailDTO.getTime1())) {
                        weight.set(Float.parseFloat(cz302ZRDetailDTO.getTime1()));
                        return "01:30:00";
                    } else if (PredicateUtil.isNotEmpty(cz302ZRDetailDTO.getTime3())) {
                        weight.set(Float.parseFloat(cz302ZRDetailDTO.getTime3()));
                        return "03:30:00";
                    } else if (PredicateUtil.isNotEmpty(cz302ZRDetailDTO.getTime5())) {
                        weight.set(Float.parseFloat(cz302ZRDetailDTO.getTime5()));
                        return "05:30:00";
                    } else if (PredicateUtil.isNotEmpty(cz302ZRDetailDTO.getTime9())) {
                        weight.set(Float.parseFloat(cz302ZRDetailDTO.getTime9()));
                        return "21:30:00";
                    } else if (PredicateUtil.isNotEmpty(cz302ZRDetailDTO.getTime11())) {
                        weight.set(Float.parseFloat(cz302ZRDetailDTO.getTime11()));
                        return "23:30:00";
                    } else {
                        return DateUtil.parseDate(System.currentTimeMillis(), DateUtil.YYYY_MM_DD_HH_MM_SS);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        cz302ZRDetailDTO.setDataTime(time);
        return NumberUtil.floatValue(weight.get());
    }

    @Override
    public void cancel() {

    }


    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_SW_TASK_KEY;
    }

}
