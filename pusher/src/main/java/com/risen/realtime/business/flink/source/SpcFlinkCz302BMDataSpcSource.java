package com.risen.realtime.business.flink.source;

import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.business.flink.cache.FlinkBeanCache;
import com.risen.realtime.business.flink.constant.WorkShopConstant;
import com.risen.realtime.framework.base.SpcSourceService;
import com.risen.realtime.framework.service.DataSourceBaseService;
import com.risen.realtime.framework.service.SystemCacheService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302BMDetailDTO;
import com.risen.realtime.resource.report.zeus.mapper.DataSpcCz302BMMapper;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 17:27
 */
public class SpcFlinkCz302BMDataSpcSource extends RichSourceFunction<SpcCz302BMDetailDTO> implements SpcSourceService {

    @Override
    public void run(SourceContext ctx) {
        FlinkBeanCache flinkBeanCache = SystemCacheService.getSystemObj(FlinkBeanCache.class);
        DataSpcCz302BMMapper bmMapper =(DataSpcCz302BMMapper)flinkBeanCache.getObj(DataSpcCz302BMMapper.class);
        DataSourceBaseService.buildDataSource(taskKey(), 5000, bmMapper, (t) -> {
            SpcCz302BMDetailDTO cz302BMDetailDTO = new SpcCz302BMDetailDTO(t);
            cz302BMDetailDTO.setTaskKey(taskKey());
            if (PredicateUtil.isNotEmpty(cz302BMDetailDTO.getProcessLine())) {
                StringBuilder builder = new StringBuilder();
                builder.append(cz302BMDetailDTO.getProcessLine());
                builder.append(WorkShopConstant.PROCESSLINE_SUFFIX);
                cz302BMDetailDTO.setProcessLine(builder.toString());
            }
            return cz302BMDetailDTO;
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
        return RedisKey.STR_SPC_CZ_302_BM_TASK_KEY;
    }

}
