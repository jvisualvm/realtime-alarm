package com.risen.realtime.framework.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 7:44
 */
@Mapper
public interface SpcDataMapper<T> extends BaseMapper<T> {

    //查询最大的id
    public Long queryMaxKey();

    public List<T> queryListByCondition(@Param("dataKey") Long dataKey, @Param(value = "limitCount") Integer limitCount);

}
