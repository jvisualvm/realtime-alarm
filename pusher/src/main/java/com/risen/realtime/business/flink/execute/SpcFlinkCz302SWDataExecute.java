package com.risen.realtime.business.flink.execute;

import com.risen.realtime.business.flink.constant.SideOutputConstant;
import com.risen.realtime.business.flink.process.CzSpc302SWRuleAProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302SWRuleBProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302SWRuleCProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302SWRuleDProcessFunction;
import com.risen.realtime.business.flink.process.alarm.CzSpc302SWAlarmProcessFunction;
import com.risen.realtime.business.flink.source.SpcFlinkCz302SWDataSpcSource;
import com.risen.realtime.framework.service.FlinkRegisterService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302SWDetailDTO;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.OutputTag;
import org.springframework.stereotype.Component;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 17:02
 */
@Component
public class SpcFlinkCz302SWDataExecute extends FlinkRegisterService {

    @Override
    public void execute() {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(flinkConfig());
        env.setParallelism(1);

        //下面是处理逻辑
        DataStreamSource<SpcCz302SWDetailDTO> sourceStream = env.addSource(new SpcFlinkCz302SWDataSpcSource());
        //定义侧输出流，按照rule字段区分是哪个规则触发的
        OutputTag<SpcCz302SWDetailDTO> sideOutputAlarmTag = new OutputTag<SpcCz302SWDetailDTO>(SideOutputConstant.SIDE_STREAM) {
        };
        //满足规则A产生告警
        SingleOutputStreamOperator<SpcCz302SWDetailDTO> dpStreamAfterRuleA = sourceStream.process(new CzSpc302SWRuleAProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302SWDetailDTO> dataStreamAfterRuleA = dpStreamAfterRuleA.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleA.process(new CzSpc302SWAlarmProcessFunction());

        //满足规则B产生告警
        SingleOutputStreamOperator<SpcCz302SWDetailDTO> streamAfterRuleB = dpStreamAfterRuleA.process(new CzSpc302SWRuleBProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302SWDetailDTO> dataStreamAfterRuleB = streamAfterRuleB.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleB.process(new CzSpc302SWAlarmProcessFunction());

        //满足规则C产生告警
        SingleOutputStreamOperator<SpcCz302SWDetailDTO> streamAfterRuleC = streamAfterRuleB.process(new CzSpc302SWRuleCProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302SWDetailDTO> dataStreamAfterRuleC = streamAfterRuleC.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleC.process(new CzSpc302SWAlarmProcessFunction());

        //满足规则D产生告警
        SingleOutputStreamOperator<SpcCz302SWDetailDTO> streamAfterRuleD = streamAfterRuleC.process(new CzSpc302SWRuleDProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302SWDetailDTO> dataStreamAfterRuleD = streamAfterRuleD.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleD.process(new CzSpc302SWAlarmProcessFunction());
        try {
            env.execute("SpcFlinkCz302SWDataExecute");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_SW_TASK_KEY;
    }

    @Override
    public boolean runWithServiceStart() {
        return true;
    }

    @Override
    public void executeAfterDisable() {
        super.deleteAllKeyByMatch();
    }
}
