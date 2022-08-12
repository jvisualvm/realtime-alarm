package com.risen.realtime.framework.event;

import com.risen.realtime.framework.base.SpcBaseDetail;
import lombok.Data;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/13 16:25
 */
@Data
public class DingDingSource implements Serializable {

    private String taskKey;
    private SpcBaseDetail value;
    private Function<String, String> msg;

    public DingDingSource(String taskKey, SpcBaseDetail value, Function<String, String> msg) {
        this.taskKey = taskKey;
        this.value = value;
        this.msg = msg;
    }
}
