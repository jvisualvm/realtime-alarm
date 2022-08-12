package com.risen.realtime.business.flink.cache;

import com.risen.helper.cache.CacheDataAbstract;
import com.risen.realtime.resource.report.analysis.mapper.*;
import com.risen.realtime.resource.report.zeus.mapper.DataSpcCz302BDHMapper;
import com.risen.realtime.resource.report.zeus.mapper.DataSpcCz302BMMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 20:35
 */
@Component
public class FlinkBeanCache extends CacheDataAbstract<Integer, Object, List<Object>> {

    @Autowired
    private DataSpcCz302DPMapper dataSpcCz30207Mapper;
    @Autowired
    private DataSpcCz302ZRMapper dataSpcCz30202Mapper;
    @Autowired
    private DataSpcCz302KSMapper dataSpcCz30203Mapper;
    @Autowired
    private DataSpcCz302ZMMapper dataSpcCz3021001Mapper;
    @Autowired
    private DataSpcCz302SWMapper dataSpcCz302SWMapper;
    @Autowired
    private DataSpcCz302BDHMapper dataSpcCz302BDHMapper;
    @Autowired
    private DataSpcCz302BMMapper dataSpcCz302BMMapper;
    @Autowired
    private DataSpcCz302ZMCache dataSpcCz302ZMCache;
    @Autowired
    private JDYDataSpcCz302KSCache jdyDataSpcCz302KSCache;

    //设置永久缓存
    public FlinkBeanCache() {
        super((long)1000000, null, null);
    }

    @Override
    protected Object getOneByKey(Integer hashCode) {
        if (Integer.valueOf(DataSpcCz302DPMapper.class.hashCode()).equals(hashCode)) {
            return dataSpcCz30207Mapper;
        }
        if (Integer.valueOf(DataSpcCz302ZRMapper.class.hashCode()).equals(hashCode)) {
            return dataSpcCz30202Mapper;
        }
        if (Integer.valueOf(DataSpcCz302KSMapper.class.hashCode()).equals(hashCode)) {
            return dataSpcCz30203Mapper;
        }
        if (Integer.valueOf(DataSpcCz302ZMMapper.class.hashCode()).equals(hashCode)) {
            return dataSpcCz3021001Mapper;
        }
        if (Integer.valueOf(DataSpcCz302SWMapper.class.hashCode()).equals(hashCode)) {
            return dataSpcCz302SWMapper;
        }
        if (Integer.valueOf(DataSpcCz302BDHMapper.class.hashCode()).equals(hashCode)) {
            return dataSpcCz302BDHMapper;
        }
        if (Integer.valueOf(DataSpcCz302BMMapper.class.hashCode()).equals(hashCode)) {
            return dataSpcCz302BMMapper;
        }
        if (Integer.valueOf(DataSpcCz302ZMCache.class.hashCode()).equals(hashCode)) {
            return dataSpcCz302ZMCache;
        }
        if (Integer.valueOf(JDYDataSpcCz302KSCache.class.hashCode()).equals(hashCode)) {
            return jdyDataSpcCz302KSCache;
        }
        return new Object();
    }

    @Override
    protected void loadCache(List<Object> objects) {

    }

    /**
     * 将对象缓存起来
     */
    @Override
    @PostConstruct
    public void loadCache() {
        put(DataSpcCz302DPMapper.class.hashCode(), dataSpcCz30207Mapper);
        put(DataSpcCz302ZRMapper.class.hashCode(), dataSpcCz30202Mapper);
        put(DataSpcCz302KSMapper.class.hashCode(), dataSpcCz30203Mapper);
        put(DataSpcCz302ZMMapper.class.hashCode(), dataSpcCz3021001Mapper);
        put(DataSpcCz302SWMapper.class.hashCode(), dataSpcCz302SWMapper);
        put(DataSpcCz302BDHMapper.class.hashCode(), dataSpcCz302BDHMapper);
        put(DataSpcCz302BMMapper.class.hashCode(), dataSpcCz302BMMapper);
        put(DataSpcCz302ZMCache.class.hashCode(), dataSpcCz302ZMCache);
        put(JDYDataSpcCz302KSCache.class.hashCode(), jdyDataSpcCz302KSCache);

    }

    public Object getObj(Class cls) {
        return get(cls.hashCode());
    }
}
