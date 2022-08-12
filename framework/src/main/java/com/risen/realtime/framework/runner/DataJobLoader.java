package com.risen.realtime.framework.runner;

import com.risen.helper.util.ThreadLocalUtil;
import com.risen.realtime.framework.cache.SystemObjectCache;
import com.risen.realtime.framework.cache.TaskSliceCache;
import com.risen.realtime.framework.manager.JobManager;
import com.risen.realtime.framework.manager.SpcDataJobManager;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 11:38
 */
@Component
@Order(value = 4)
@AllArgsConstructor
public class DataJobLoader implements CommandLineRunner {

    private JobManager jobManager;
    private SystemObjectCache systemObjectCache;

    @Override
    public void run(String... args) throws Exception {
        //TODO 暂时没有kafka，只能先写个死循环，用flink处理，后续有了kafka需要重构
        List<SpcDataJobManager> jobList = SpcDataJobManager.getImplTree();
        //框架自动放入cache
        ThreadLocalUtil.inLocal.set(systemObjectCache);
        if (!CollectionUtils.isEmpty(jobList)) {
            jobList.forEach(job -> {
                //初期版本只需要加在系统定时任务
                //不对任务重试任务进行分片
                if (canLoad(job)) {
                    jobManager.removeJob(job.taskKey());
                    jobManager.startJob(job.taskKey(), job.getClass());
                }
            });
        }

    }


    private boolean canLoad(SpcDataJobManager job) {
        return TaskSliceCache.NONE_SLICE_TASK_LIST.contains(job.taskKey()) || (job.isSystem() && TaskSliceCache.containTask(job.taskKey()));
    }


}
