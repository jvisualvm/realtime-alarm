package com.risen.realtime.business.flink.execute;

import com.risen.realtime.business.flink.constant.SideOutputConstant;
import com.risen.realtime.business.flink.process.CzSpc302BMRuleAProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302BMRuleBProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302BMRuleCProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302BMRuleDProcessFunction;
import com.risen.realtime.business.flink.process.alarm.CzSpc302BMAlarmProcessFunction;
import com.risen.realtime.business.flink.source.SpcFlinkCz302BMDataSpcSource;
import com.risen.realtime.framework.service.FlinkRegisterService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302BMDetailDTO;
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
public class SpcFlinkCz302BMDataExecute extends FlinkRegisterService {


    @Override
    public void execute() {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(flinkConfig());
        env.setParallelism(1);

        //下面是处理逻辑
        DataStreamSource<SpcCz302BMDetailDTO> sourceStream = env.addSource(new SpcFlinkCz302BMDataSpcSource());
        //定义侧输出流，按照rule字段区分是哪个规则触发的
        OutputTag<SpcCz302BMDetailDTO> sideOutputAlarmTag = new OutputTag<SpcCz302BMDetailDTO>(SideOutputConstant.SIDE_STREAM) {
        };
        //满足规则A产生告警
        SingleOutputStreamOperator<SpcCz302BMDetailDTO> dpStreamAfterRuleA = sourceStream.process(new CzSpc302BMRuleAProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302BMDetailDTO> dataStreamAfterRuleA = dpStreamAfterRuleA.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleA.process(new CzSpc302BMAlarmProcessFunction());

        //满足规则B产生告警
        SingleOutputStreamOperator<SpcCz302BMDetailDTO> streamAfterRuleB = dpStreamAfterRuleA.process(new CzSpc302BMRuleBProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302BMDetailDTO> dataStreamAfterRuleB = streamAfterRuleB.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleB.process(new CzSpc302BMAlarmProcessFunction());

        //满足规则C产生告警
        SingleOutputStreamOperator<SpcCz302BMDetailDTO> streamAfterRuleC = streamAfterRuleB.process(new CzSpc302BMRuleCProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302BMDetailDTO> dataStreamAfterRuleC = streamAfterRuleC.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleC.process(new CzSpc302BMAlarmProcessFunction());

        //满足规则D产生告警
        SingleOutputStreamOperator<SpcCz302BMDetailDTO> streamAfterRuleD = streamAfterRuleC.process(new CzSpc302BMRuleDProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302BMDetailDTO> dataStreamAfterRuleD = streamAfterRuleD.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleD.process(new CzSpc302BMAlarmProcessFunction());
        try {
            env.execute("SpcFlinkCz302BMDataExecute");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_BM_TASK_KEY;
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
