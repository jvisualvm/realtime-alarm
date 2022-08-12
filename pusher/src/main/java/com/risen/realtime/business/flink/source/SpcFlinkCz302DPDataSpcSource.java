package com.risen.realtime.business.flink.source;

import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.business.flink.cache.FlinkBeanCache;
import com.risen.realtime.business.flink.constant.WorkShopConstant;
import com.risen.realtime.framework.base.SpcSourceService;
import com.risen.realtime.framework.service.DataSourceBaseService;
import com.risen.realtime.framework.service.SystemCacheService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302DPDetailDTO;
import com.risen.realtime.resource.report.analysis.mapper.DataSpcCz302DPMapper;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 17:27
 */
public class SpcFlinkCz302DPDataSpcSource extends RichSourceFunction<SpcCz302DPDetailDTO> implements SpcSourceService {

    @Override
    public void run(SourceContext ctx) {
        FlinkBeanCache flinkBeanCache = SystemCacheService.getSystemObj(FlinkBeanCache.class);
        DataSpcCz302DPMapper dataSpcCz30207Mapper = (DataSpcCz302DPMapper) flinkBeanCache.getObj(DataSpcCz302DPMapper.class);
        DataSourceBaseService.buildDataSource(taskKey(), 5000, dataSpcCz30207Mapper, (t) -> {
            SpcCz302DPDetailDTO spc207DetailDTO = new SpcCz302DPDetailDTO(t);
            spc207DetailDTO.setTaskKey(taskKey());
            if (PredicateUtil.isNotEmpty(spc207DetailDTO.getProcessLine())) {
                StringBuilder builder = new StringBuilder();
                builder.append(spc207DetailDTO.getProcessLine());
                builder.append(WorkShopConstant.PROCESSLINE_SUFFIX);
                spc207DetailDTO.setProcessLine(builder.toString());
            }
            return spc207DetailDTO;
        }, (t) -> {
            return true;
        }, (t) -> {
            return true;
        }, (maxKey) -> {
            return maxKey.stream().mapToLong(l -> l.getDataKey()).max().getAsLong();
        }, ctx);
    }

    @Override
    public void cancel() {

    }


    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_DP_TASK_KEY;
    }

}
