package com.risen.realtime.business.flink.source;

import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.business.flink.cache.FlinkBeanCache;
import com.risen.realtime.business.flink.constant.WorkShopConstant;
import com.risen.realtime.framework.base.SpcSourceService;
import com.risen.realtime.framework.service.DataSourceBaseService;
import com.risen.realtime.framework.service.SystemCacheService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302BDHDetailDTO;
import com.risen.realtime.resource.report.zeus.mapper.DataSpcCz302BDHMapper;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 17:27
 */
public class SpcFlinkCz302BDHDataSpcSource extends RichSourceFunction<SpcCz302BDHDetailDTO> implements SpcSourceService {

    @Override
    public void run(SourceContext ctx) {
        FlinkBeanCache flinkBeanCache = SystemCacheService.getSystemObj(FlinkBeanCache.class);
        DataSpcCz302BDHMapper bdhMapper = (DataSpcCz302BDHMapper) flinkBeanCache.getObj(DataSpcCz302BDHMapper.class);
        DataSourceBaseService.buildDataSource(taskKey(), 5000, bdhMapper, (t) -> {
            SpcCz302BDHDetailDTO cz302BDHDetailDTO = new SpcCz302BDHDetailDTO(t);
            cz302BDHDetailDTO.setTaskKey(taskKey());
            if (PredicateUtil.isNotEmpty(cz302BDHDetailDTO.getProcessLine())) {
                StringBuilder builder = new StringBuilder();
                builder.append(cz302BDHDetailDTO.getProcessLine());
                builder.append(WorkShopConstant.PROCESSLINE_SUFFIX);
                cz302BDHDetailDTO.setProcessLine(builder.toString());
            }
            return cz302BDHDetailDTO;
        }, (t) -> {
            return true;
        }, (t) -> {
            return true;
        }, (maxKeyList) -> {
            return maxKeyList.stream().mapToLong(l -> l.getDataKey()).max().getAsLong();
        }, ctx);
    }

    @Override
    public void cancel() {

    }


    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_BDH_TASK_KEY;
    }

}
