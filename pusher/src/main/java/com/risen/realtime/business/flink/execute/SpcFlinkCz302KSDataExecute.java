package com.risen.realtime.business.flink.execute;

import com.risen.realtime.business.flink.constant.SideOutputConstant;
import com.risen.realtime.business.flink.process.CzSpc302KSRuleAProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302KSRuleBProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302KSRuleCProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302KSRuleDProcessFunction;
import com.risen.realtime.business.flink.process.alarm.CzSpc302KSAlarmProcessFunction;
import com.risen.realtime.business.flink.source.SpcFlinkCz302KSDataSpcSource;
import com.risen.realtime.framework.service.FlinkRegisterService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302KSDetailDTO;
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
public class SpcFlinkCz302KSDataExecute extends FlinkRegisterService {

    @Override
    public void execute() {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(flinkConfig());
        env.setParallelism(1);

        //下面是处理逻辑
        DataStreamSource<SpcCz302KSDetailDTO> sourceStream = env.addSource(new SpcFlinkCz302KSDataSpcSource());
        //定义侧输出流，按照rule字段区分是哪个规则触发的
        OutputTag<SpcCz302KSDetailDTO> sideOutputAlarmTag = new OutputTag<SpcCz302KSDetailDTO>(SideOutputConstant.SIDE_STREAM) {
        };
        //满足规则A产生告警
        SingleOutputStreamOperator<SpcCz302KSDetailDTO> dpStreamAfterRuleA = sourceStream.process(new CzSpc302KSRuleAProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302KSDetailDTO> dataStreamAfterRuleA = dpStreamAfterRuleA.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleA.process(new CzSpc302KSAlarmProcessFunction());

        //满足规则B产生告警
        SingleOutputStreamOperator<SpcCz302KSDetailDTO> streamAfterRuleB = dpStreamAfterRuleA.process(new CzSpc302KSRuleBProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302KSDetailDTO> dataStreamAfterRuleB = streamAfterRuleB.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleB.process(new CzSpc302KSAlarmProcessFunction());

        //满足规则C产生告警
        SingleOutputStreamOperator<SpcCz302KSDetailDTO> streamAfterRuleC = streamAfterRuleB.process(new CzSpc302KSRuleCProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302KSDetailDTO> dataStreamAfterRuleC = streamAfterRuleC.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleC.process(new CzSpc302KSAlarmProcessFunction());

        //满足规则D产生告警
        SingleOutputStreamOperator<SpcCz302KSDetailDTO> streamAfterRuleD = streamAfterRuleC.process(new CzSpc302KSRuleDProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302KSDetailDTO> dataStreamAfterRuleD = streamAfterRuleD.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleD.process(new CzSpc302KSAlarmProcessFunction());
        try {
            env.execute("SpcFlinkCz302KSDataExecute");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_KS_TASK_KEY;
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
