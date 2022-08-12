package com.risen.realtime.business.flink.execute;

import com.risen.realtime.business.flink.constant.SideOutputConstant;
import com.risen.realtime.business.flink.process.CzSpc302BDHRuleAProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302BDHRuleBProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302BDHRuleCProcessFunction;
import com.risen.realtime.business.flink.process.CzSpc302BDHRuleDProcessFunction;
import com.risen.realtime.business.flink.process.alarm.CzSpc302BDHAlarmProcessFunction;
import com.risen.realtime.business.flink.source.SpcFlinkCz302BDHDataSpcSource;
import com.risen.realtime.framework.service.FlinkRegisterService;
import com.risen.realtime.resource.contant.RedisKey;
import com.risen.realtime.resource.dto.SpcCz302BDHDetailDTO;
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
public class SpcFlinkCz302BDHDataExecute extends FlinkRegisterService {


    @Override
    public void execute() {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(flinkConfig());
        env.setParallelism(1);

        //下面是处理逻辑
        DataStreamSource<SpcCz302BDHDetailDTO> sourceStream = env.addSource(new SpcFlinkCz302BDHDataSpcSource());
        //定义侧输出流，按照rule字段区分是哪个规则触发的
        OutputTag<SpcCz302BDHDetailDTO> sideOutputAlarmTag = new OutputTag<SpcCz302BDHDetailDTO>(SideOutputConstant.SIDE_STREAM) {
        };
        //满足规则A产生告警
        SingleOutputStreamOperator<SpcCz302BDHDetailDTO> dpStreamAfterRuleA = sourceStream.process(new CzSpc302BDHRuleAProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302BDHDetailDTO> dataStreamAfterRuleA = dpStreamAfterRuleA.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleA.process(new CzSpc302BDHAlarmProcessFunction());

        //满足规则B产生告警
        SingleOutputStreamOperator<SpcCz302BDHDetailDTO> streamAfterRuleB = dpStreamAfterRuleA.process(new CzSpc302BDHRuleBProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302BDHDetailDTO> dataStreamAfterRuleB = streamAfterRuleB.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleB.process(new CzSpc302BDHAlarmProcessFunction());

        //满足规则C产生告警
        SingleOutputStreamOperator<SpcCz302BDHDetailDTO> streamAfterRuleC = streamAfterRuleB.process(new CzSpc302BDHRuleCProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302BDHDetailDTO> dataStreamAfterRuleC = streamAfterRuleC.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleC.process(new CzSpc302BDHAlarmProcessFunction());

        //满足规则D产生告警
        SingleOutputStreamOperator<SpcCz302BDHDetailDTO> streamAfterRuleD = streamAfterRuleC.process(new CzSpc302BDHRuleDProcessFunction());
        //获取侧输出流，并推送钉钉告警
        DataStream<SpcCz302BDHDetailDTO> dataStreamAfterRuleD = streamAfterRuleD.getSideOutput(sideOutputAlarmTag);
        dataStreamAfterRuleD.process(new CzSpc302BDHAlarmProcessFunction());
        try {
            env.execute("SpcFlinkCz302BDHDataExecute");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String taskKey() {
        return RedisKey.STR_SPC_CZ_302_BDH_TASK_KEY;
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
