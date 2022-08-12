package com.risen.realtime.framework.controller;

import com.risen.helper.response.Result;
import com.risen.helper.response.ResultProxy;
import com.risen.realtime.framework.service.SystemCacheRefreshService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/12 12:59
 */
@RestController
@RequestMapping(value = "/refresh/cache")
@AllArgsConstructor
@Api(tags = "系统缓存刷新接口")
public class SystemCacheController {

    private SystemCacheRefreshService service;

    @PutMapping(value = "/channel")
    @ApiOperation(tags = "刷新频道缓存", value = "刷新频道缓存")
    public Result refreshChannelCache() {
        service.refreshChannelCache();
        return ResultProxy.build().successIgnoreBody();
    }


    @PutMapping(value = "/config")
    @ApiOperation(tags = "刷新配置缓存", value = "刷新配置缓存")
    public Result refreshConfigCache() {
        service.refreshConfigCache();
        return ResultProxy.build().successIgnoreBody();
    }


    @PutMapping(value = "/rule")
    @ApiOperation(tags = "刷新规则缓存", value = "刷新规则缓存")
    public Result refreshRuleCache() {
        service.refreshRuleCache();
        return ResultProxy.build().successIgnoreBody();
    }

    @PutMapping(value = "/task")
    @ApiOperation(tags = "刷新任务缓存", value = "刷新任务缓存")
    public Result refreshTaskCache() {
        service.refreshTaskCache();
        return ResultProxy.build().successIgnoreBody();
    }


}
