package com.risen.realtime.framework.dto;

import lombok.Data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/14 16:01
 */
@Data
public class TaskSliceDTO {

    private Integer serverCount;
    private Set<String> taskKeySet = Collections.synchronizedSet(new HashSet<>());
    private Set<String> disableSet = Collections.synchronizedSet(new HashSet<>());

    public void update(Integer serverCount, Set<String> taskKeySet) {
        this.serverCount = serverCount;
        this.taskKeySet.addAll(taskKeySet);
    }

}
