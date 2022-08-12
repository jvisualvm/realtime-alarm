package com.risen.realtime.business.flink.cache;

import com.risen.helper.cache.CacheDataAbstract;
import com.risen.helper.util.LogUtil;
import com.risen.realtime.resource.report.analysis.entity.DataSpcCz302ZMEntity;
import com.risen.realtime.resource.report.analysis.mapper.DataSpcCz302ZMMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangxin
 * @since 2022-06-30
 */
@Component
public class DataSpcCz302ZMCache extends CacheDataAbstract<Integer, DataSpcCz302ZMEntity, List<DataSpcCz302ZMEntity>> {


    @Autowired
    private DataSpcCz302ZMMapper dataSpcCz3021001Mapper;

    //设置永久缓存
    public DataSpcCz302ZMCache() {
        super((long)1000000, null, null);
    }


    @Override
    protected DataSpcCz302ZMEntity getOneByKey(Integer integer) {
        return dataSpcCz3021001Mapper.queryOneByCondition();
    }

    @Override
    protected void loadCache(List<DataSpcCz302ZMEntity> objects) {

    }

    @Override
    public void loadCache() {
        LogUtil.info("DataSpcCz302ZMCache start load cache");
        DataSpcCz302ZMEntity result = dataSpcCz3021001Mapper.queryOneByCondition();
        Optional.of(result).ifPresent(item -> {
            put(DataSpcCz302ZMMapper.class.hashCode(), result);
        });
    }
}
