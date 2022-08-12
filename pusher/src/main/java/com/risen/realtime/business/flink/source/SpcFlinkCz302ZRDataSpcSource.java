package com.risen.realtime.business.flink.source;

import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.business.flink.cache.FlinkBeanCache;
import com.risen.realtime.business.flink.constant.WorkShopConstant;
import com.risen.realtime.framework.base.SpcSourceService;
import com.risen.realtime.framework.service.DataSourceBaseService;
import com.risen.realtime.framework.service.SystemCacheService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302ZRDetailDTO;
import com.risen.realtime.resource.report.analysis.mapper.DataSpcCz302ZRMapper;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 17:27
 */
public class SpcFlinkCz302ZRDataSpcSource extends RichSourceFunction<SpcCz302ZRDetailDTO> implements SpcSourceService {

    @Override
    public void run(SourceContext ctx) {
        FlinkBeanCache flinkBeanCache = SystemCacheService.getSystemObj(FlinkBeanCache.class);
        DataSpcCz302ZRMapper dataSpcCz30202Mapper = (DataSpcCz302ZRMapper) flinkBeanCache.getObj(DataSpcCz302ZRMapper.class);
        DataSourceBaseService.buildDataSource(taskKey(), 5000, dataSpcCz30202Mapper, (t) -> {
            SpcCz302ZRDetailDTO cz302ZRDetailDTO = new SpcCz302ZRDetailDTO(t);
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

    @Override
    public void cancel() {

    }


    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_ZR_TASK_KEY;
    }

}
