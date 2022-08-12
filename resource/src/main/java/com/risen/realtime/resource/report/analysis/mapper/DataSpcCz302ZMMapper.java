package com.risen.realtime.resource.report.analysis.mapper;

import com.risen.realtime.framework.base.SpcDataMapper;
import com.risen.realtime.resource.report.analysis.entity.DataSpcCz302ZMEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhangxin
 * @since 2022-06-30
 */
@Mapper
public interface DataSpcCz302ZMMapper extends SpcDataMapper<DataSpcCz302ZMEntity> {

    public void syncAggregationData(@Param("dataKey") Long dataKey);

    public DataSpcCz302ZMEntity queryOneByCondition();

}
