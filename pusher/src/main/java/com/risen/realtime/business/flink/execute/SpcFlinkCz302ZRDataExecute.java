package com.risen.realtime.business.flink.execute;

import com.risen.realtime.business.flink.constant.SideOutputConstant;
import com.risen.realtime.business.flink.process.CzSpc302ZRRuleAProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302ZRRuleBProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302ZRRuleCProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302ZRRuleDProcessFunction;
import com.risen.realtime.business.flink.process.alarm.CzSpc302ZRAlarmProcessFunction;
import com.risen.realtime.business.flink.source.SpcFlinkCz302ZRDataSpcSource;
import com.risen.realtime.framework.service.FlinkRegisterService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302ZRDetailDTO;
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
public class SpcFlinkCz302ZRDataExecute extends FlinkRegisterService {

    @Override
    public void execute() {


        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(flinkConfig());
        env.setParallelism(1);

        //下面是处理逻辑
        DataStreamSource<SpcCz302ZRDetailDTO> sourceStream = env.addSource(new SpcFlinkCz302ZRDataSpcSource());
        //定义侧输出流，按照rule字段区分是哪个规则触发的
        OutputTag<SpcCz302ZRDetailDTO> sideOutputAlarmTag = new OutputTag<SpcCz302ZRDetailDTO>(SideOutputConstant.SIDE_STREAM) {
        };
        //满足规则A产生告警
        SingleOutputStreamOperator<SpcCz302ZRDetailDTO> dpStreamAfterRuleA = sourceStream.process(new CzSpc302ZRRuleAProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302ZRDetailDTO> dataStreamAfterRuleA = dpStreamAfterRuleA.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleA.process(new CzSpc302ZRAlarmProcessFunction());

        //满足规则B产生告警
        SingleOutputStreamOperator<SpcCz302ZRDetailDTO> streamAfterRuleB = dpStreamAfterRuleA.process(new CzSpc302ZRRuleBProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302ZRDetailDTO> dataStreamAfterRuleB = streamAfterRuleB.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleB.process(new CzSpc302ZRAlarmProcessFunction());

        //满足规则C产生告警
        SingleOutputStreamOperator<SpcCz302ZRDetailDTO> streamAfterRuleC = streamAfterRuleB.process(new CzSpc302ZRRuleCProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302ZRDetailDTO> dataStreamAfterRuleC = streamAfterRuleC.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleC.process(new CzSpc302ZRAlarmProcessFunction());

        //满足规则D产生告警
        SingleOutputStreamOperator<SpcCz302ZRDetailDTO> streamAfterRuleD = streamAfterRuleC.process(new CzSpc302ZRRuleDProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302ZRDetailDTO> dataStreamAfterRuleD = streamAfterRuleD.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleD.process(new CzSpc302ZRAlarmProcessFunction());
        try {
            env.execute("SpcFlinkCz302ZRDataExecute");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_ZR_TASK_KEY;
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
