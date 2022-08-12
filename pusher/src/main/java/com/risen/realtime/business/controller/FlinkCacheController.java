package com.risen.realtime.business.controller;

import com.risen.helper.response.Result;
import com.risen.helper.response.ResultProxy;
import com.risen.realtime.business.service.FlinkCacheRefreshService;
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
@RequestMapping(value = "/push/refresh/cache")
@AllArgsConstructor
@Api(tags = "业务-缓存刷新接口")
public class
FlinkCacheController {

    private FlinkCacheRefreshService service;

    @PutMapping(value = "/cz302/ks/jdy")
    @ApiOperation(tags = "刷新滁州302扩散简道云缓存", value = "刷新滁州302扩散简道云缓存")
    public Result refreshCz302KsJdyCache() {
        service.refreshCz302KsJdyCache();
        return ResultProxy.build().successIgnoreBody();
    }


}
