package com.risen.realtime.business.flink.source;

import com.risen.helper.util.NumberUtil;
import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.business.flink.cache.DataSpcCz302ZMCache;
import com.risen.realtime.business.flink.cache.FlinkBeanCache;
import com.risen.realtime.business.flink.constant.WorkShopConstant;
import com.risen.realtime.framework.base.SpcSourceService;
import com.risen.realtime.framework.service.DataSourceBaseService;
import com.risen.realtime.framework.service.SystemCacheService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302ZMDetailDTO;
import com.risen.realtime.resource.report.analysis.entity.DataSpcCz302ZMEntity;
import com.risen.realtime.resource.report.analysis.mapper.DataSpcCz302ZMMapper;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.Optional;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 17:27
 */
public class SpcFlinkCz302ZMDataSpcSource extends RichSourceFunction<SpcCz302ZMDetailDTO> implements SpcSourceService {

    @Override
    public void run(SourceContext ctx) {
        FlinkBeanCache flinkBeanCache = SystemCacheService.getSystemObj(FlinkBeanCache.class);
        DataSpcCz302ZMCache cache = (DataSpcCz302ZMCache)flinkBeanCache.getObj(DataSpcCz302ZMCache.class);
        DataSpcCz302ZMMapper dataSpcCz3021001Mapper =(DataSpcCz302ZMMapper)flinkBeanCache.getObj(DataSpcCz302ZMMapper.class);
        DataSourceBaseService.buildDataSource(taskKey(), 5000, dataSpcCz3021001Mapper, (t) -> {
            SpcCz302ZMDetailDTO zmDetailDTO = new SpcCz302ZMDetailDTO(t);
            zmDetailDTO.setTaskKey(taskKey());
            if (PredicateUtil.isNotEmpty(zmDetailDTO.getProcessLine())) {
                StringBuilder builder = new StringBuilder();
                builder.append(zmDetailDTO.getProcessLine());
                builder.append(WorkShopConstant.PROCESSLINE_SUFFIX);
                zmDetailDTO.setProcessLine(builder.toString());
                updateControlValue(zmDetailDTO, cache);
            }
            return zmDetailDTO;
        }, (t) -> {
            return true;
        }, (t) -> {
            return true;
        }, (maxKey) -> {
            return maxKey.stream().mapToLong(l -> l.getDataKey()).max().getAsLong();
        }, ctx);
    }


    private void updateControlValue(SpcCz302ZMDetailDTO cz302ZRDetailDTO, DataSpcCz302ZMCache cache) {
        try {
            DataSpcCz302ZMEntity dataSpcCz302ZMEntity = cache.get(DataSpcCz302ZMMapper.class.hashCode());
            Optional.of(dataSpcCz302ZMEntity).ifPresent(item -> {
                //氧化硅折射率中心值zs_yh_cn
                cz302ZRDetailDTO.setMiddleReflexRate(NumberUtil.floatValue(item.getZsYhCn()));
                //氧化硅膜厚中心值mh_yh_cn
                cz302ZRDetailDTO.setMiddleThickness(NumberUtil.floatValue(item.getMhYhCn()));
                //氧化硅折射率控制上限 zs_yh_a
                cz302ZRDetailDTO.setContrlUpReflexRate(NumberUtil.floatValue(item.getZsYhA()));
                //氧化硅折射率控制下限 zs_yh_b
                cz302ZRDetailDTO.setContrlDownReflexRate(NumberUtil.floatValue(item.getZsYhB()));
                //氧化硅膜厚控制上限mh_yh_a
                cz302ZRDetailDTO.setControlUpThickness(NumberUtil.floatValue(item.getMhYhA()));
                //氧化硅膜厚控制下限mh_yh_b
                cz302ZRDetailDTO.setControlDownThickness(NumberUtil.floatValue(item.getMhYhB()));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel() {

    }


    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_ZM_TASK_KEY;
    }

}
