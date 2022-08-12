package com.risen.realtime.business.flink.execute;

import com.risen.realtime.business.flink.constant.SideOutputConstant;
import com.risen.realtime.business.flink.process.CzSpc302ZMRuleAProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302ZMRuleBProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302ZMRuleCProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302ZMRuleDProcessFunction;
import com.risen.realtime.business.flink.process.alarm.CzSpc302ZMAlarmProcessFunction;
import com.risen.realtime.business.flink.source.SpcFlinkCz302ZMDataSpcSource;
import com.risen.realtime.framework.service.FlinkRegisterService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302ZMDetailDTO;
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
public class SpcFlinkCz302ZMDataExecute extends FlinkRegisterService {

    @Override
    public void execute() {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(flinkConfig());
        env.setParallelism(1);

        //下面是处理逻辑
        DataStreamSource<SpcCz302ZMDetailDTO> sourceStream = env.addSource(new SpcFlinkCz302ZMDataSpcSource());
        //定义侧输出流，按照rule字段区分是哪个规则触发的
        OutputTag<SpcCz302ZMDetailDTO> sideOutputAlarmTag = new OutputTag<SpcCz302ZMDetailDTO>(SideOutputConstant.SIDE_STREAM) {
        };
        //满足规则A产生告警
        SingleOutputStreamOperator<SpcCz302ZMDetailDTO> dpStreamAfterRuleA = sourceStream.process(new CzSpc302ZMRuleAProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302ZMDetailDTO> dataStreamAfterRuleA = dpStreamAfterRuleA.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleA.process(new CzSpc302ZMAlarmProcessFunction());

        //满足规则B产生告警
        SingleOutputStreamOperator<SpcCz302ZMDetailDTO> streamAfterRuleB = dpStreamAfterRuleA.process(new CzSpc302ZMRuleBProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302ZMDetailDTO> dataStreamAfterRuleB = streamAfterRuleB.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleB.process(new CzSpc302ZMAlarmProcessFunction());

        //满足规则C产生告警
        SingleOutputStreamOperator<SpcCz302ZMDetailDTO> streamAfterRuleC = streamAfterRuleB.process(new CzSpc302ZMRuleCProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302ZMDetailDTO> dataStreamAfterRuleC = streamAfterRuleC.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleC.process(new CzSpc302ZMAlarmProcessFunction());

        //满足规则D产生告警
        SingleOutputStreamOperator<SpcCz302ZMDetailDTO> streamAfterRuleD = streamAfterRuleC.process(new CzSpc302ZMRuleDProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302ZMDetailDTO> dataStreamAfterRuleD = streamAfterRuleD.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleD.process(new CzSpc302ZMAlarmProcessFunction());
        try {
            env.execute("SpcFlinkCz302ZMDataExecute");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_ZM_TASK_KEY;
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
