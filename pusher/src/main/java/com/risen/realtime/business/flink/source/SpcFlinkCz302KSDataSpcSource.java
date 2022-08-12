package com.risen.realtime.business.flink.source;

import com.risen.helper.util.PredicateUtil;
import com.risen.realtime.business.flink.cache.FlinkBeanCache;
import com.risen.realtime.business.flink.cache.JDYDataSpcCz302KSCache;
import com.risen.realtime.business.flink.constant.OnlineStatusEnum;
import com.risen.realtime.business.flink.constant.WorkShopConstant;
import com.risen.realtime.framework.base.SpcSourceService;
import com.risen.realtime.framework.service.DataSourceBaseService;
import com.risen.realtime.framework.service.SystemCacheService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302KSDetailDTO;
import com.risen.realtime.resource.dto.SpcCz302KsJDYTransferDetailDTO;
import com.risen.realtime.resource.report.analysis.entity.DataSpcCz302KSEntity;
import com.risen.realtime.resource.report.analysis.mapper.DataSpcCz302KSMapper;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 17:27
 */
public class SpcFlinkCz302KSDataSpcSource extends RichSourceFunction<SpcCz302KSDetailDTO> implements SpcSourceService {

    @Override
    public void run(SourceContext ctx) {
        FlinkBeanCache flinkBeanCache = SystemCacheService.getSystemObj(FlinkBeanCache.class);
        DataSpcCz302KSMapper dataSpcCz30203Mapper = (DataSpcCz302KSMapper) flinkBeanCache.getObj(DataSpcCz302KSMapper.class);
        DataSourceBaseService.buildDataSource(taskKey(), 5000, dataSpcCz30203Mapper, (t) -> {
            SpcCz302KSDetailDTO cz302ZRDetailDTO = new SpcCz302KSDetailDTO(t);
            //填充简道云信息
            fillJdyDetail(flinkBeanCache, cz302ZRDetailDTO);

            fillOnlineStatus(t, cz302ZRDetailDTO);

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
            //剔除方阻值为0、0.1的值
            boolean isContinue = true;
            if (PredicateUtil.isNotEmpty(t.getAvg())) {
                isContinue = !(t.getAvg() == 0 || t.getAvg() == 0.1);
            }
            return isContinue;
        }, (maxKey) -> {
            return maxKey.stream().mapToLong(l -> l.getDataKey()).max().getAsLong();
        }, ctx);
    }


    private void fillJdyDetail(FlinkBeanCache flinkBeanCache, SpcCz302KSDetailDTO cz302ZRDetailDTO) {
        JDYDataSpcCz302KSCache cache = (JDYDataSpcCz302KSCache) flinkBeanCache.get(JDYDataSpcCz302KSCache.class.hashCode());
        SpcCz302KsJDYTransferDetailDTO result = cache.get(cache.createCacheKey());
        cz302ZRDetailDTO.setMiddleRate(result.getMiddleRate());
        cz302ZRDetailDTO.setContrlUpRate(result.getContrlUpRate());
        cz302ZRDetailDTO.setContrlDownRate(result.getContrlDownRate());
    }


    private void fillOnlineStatus(DataSpcCz302KSEntity dataSpcCz30207, SpcCz302KSDetailDTO cz302ZRDetailDTO) {
        //on:在线方阻测试仪，off：离线方阻测试仪
        if (OnlineStatusEnum.ONLINE.getCode().equals(dataSpcCz30207.getAuxInstrument())) {
            cz302ZRDetailDTO.setOnline(OnlineStatusEnum.ONLINE.getMsg());
        }
        if (OnlineStatusEnum.OFFLINE.getCode().equals(dataSpcCz30207.getAuxInstrument())) {
            cz302ZRDetailDTO.setOnline(OnlineStatusEnum.OFFLINE.getMsg());
        }
    }


    @Override
    public void cancel() {

    }


    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_KS_TASK_KEY;
    }

}
