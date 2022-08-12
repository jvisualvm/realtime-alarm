package com.risen.realtime.business.flink.execute;

import com.risen.realtime.business.flink.constant.SideOutputConstant;
import com.risen.realtime.business.flink.process.CzSpc302DPRuleAProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302DPRuleBProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302DPRuleCProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302DPRuleDProcessFunction;
import com.risen.realtime.business.flink.process.alarm.CzSpc302DPAlarmProcessFunction;
import com.risen.realtime.business.flink.source.SpcFlinkCz302DPDataSpcSource;
import com.risen.realtime.framework.service.FlinkRegisterService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302DPDetailDTO;
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
public class SpcFlinkCz302DPDataExecute extends FlinkRegisterService {


    @Override
    public void execute() {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(flinkConfig());
        env.setParallelism(1);

        //下面是处理逻辑
        DataStreamSource<SpcCz302DPDetailDTO> sourceStream = env.addSource(new SpcFlinkCz302DPDataSpcSource());
        //定义侧输出流，按照rule字段区分是哪个规则触发的
        OutputTag<SpcCz302DPDetailDTO> sideOutputAlarmTag = new OutputTag<SpcCz302DPDetailDTO>(SideOutputConstant.SIDE_STREAM) {
        };
        //满足规则A产生告警
        SingleOutputStreamOperator<SpcCz302DPDetailDTO> dpStreamAfterRuleA = sourceStream.process(new CzSpc302DPRuleAProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302DPDetailDTO> dataStreamAfterRuleA = dpStreamAfterRuleA.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleA.process(new CzSpc302DPAlarmProcessFunction());

        //满足规则B产生告警
        SingleOutputStreamOperator<SpcCz302DPDetailDTO> streamAfterRuleB = dpStreamAfterRuleA.process(new CzSpc302DPRuleBProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302DPDetailDTO> dataStreamAfterRuleB = streamAfterRuleB.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleB.process(new CzSpc302DPAlarmProcessFunction());

        //满足规则C产生告警
        SingleOutputStreamOperator<SpcCz302DPDetailDTO> streamAfterRuleC = streamAfterRuleB.process(new CzSpc302DPRuleCProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302DPDetailDTO> dataStreamAfterRuleC = streamAfterRuleC.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleC.process(new CzSpc302DPAlarmProcessFunction());

        //满足规则D产生告警
        SingleOutputStreamOperator<SpcCz302DPDetailDTO> streamAfterRuleD = streamAfterRuleC.process(new CzSpc302DPRuleDProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302DPDetailDTO> dataStreamAfterRuleD = streamAfterRuleD.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleD.process(new CzSpc302DPAlarmProcessFunction());
        try {
            env.execute("spcFlinkCz207DPDataExecute");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_DP_TASK_KEY;
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
