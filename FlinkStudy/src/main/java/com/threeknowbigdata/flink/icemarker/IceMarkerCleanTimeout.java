package com.threeknowbigdata.flink.icemarker;

import com.threeknowbigdata.flink.order_pay_detect.bean.OrderEvent;
import com.threeknowbigdata.flink.order_pay_detect.bean.OrderResult;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.PatternTimeoutFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

import java.util.List;
import java.util.Map;


/**
 * 类描述：
 *
 * @ClassName OrderPayTimeout
 * @Description:
 * @Author: 土哥
 * @Date: 2021/9/1 下午2:51
 */
public class IceMarkerCleanTimeout {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);

        // 读取数据并转换成POJO类型
        DataStreamSource<String> stringDataStreamSource = env.readTextFile("D:\\flink_second_understand\\FlinkStudy\\src\\main\\java\\com\\threeknowbigdata\\flink\\icemarker\\OrderLog.csv");
        KeyedStream<IceMarker, Long> iceMarkerLongKeyedStream = stringDataStreamSource.map(new MapFunction<String, IceMarker>() {
            @Override
            public IceMarker map(String line) throws Exception {
                String[] fields = line.split(",");
                return new IceMarker(new Long(fields[0]), fields[1], new Long(fields[2]));
            }
        }).assignTimestampsAndWatermarks(new AscendingTimestampExtractor<IceMarker>() {
            @Override
            public long extractAscendingTimestamp(IceMarker element) {
                return element.getTimestamp() * 1000L;
            }
        }).keyBy(IceMarker::getIceId);

        System.out.println(iceMarkerLongKeyedStream);

        // 1. 定义一个带时间限制的模式
        Pattern<IceMarker, IceMarker> iceMarkerIceMarkerPattern = Pattern
                .<IceMarker>begin("begin").where(new SimpleCondition<IceMarker>() {
                    @Override
                    public boolean filter(IceMarker value) throws Exception {
                        return "10".equals(value.getElectric());
                    }
                })
                .followedBy("next").where(new SimpleCondition<IceMarker>() {
                    @Override
                    public boolean filter(IceMarker value) throws Exception {
                        return "10".equals(value.getElectric());
                    }
                }).notOccur(Time.minutes(15));

        // 2. 定义侧输出流标签，用来表示超时事件
        OutputTag<IceMarkerResult> orderTimeoutTag = new OutputTag<IceMarkerResult>("clean-timeout"){};

        // 3. 将pattern应用到输入数据流上，得到pattern stream
        PatternStream<IceMarker> patternStream = CEP.pattern(iceMarkerLongKeyedStream, iceMarkerIceMarkerPattern);

        // 4. 调用select方法，实现对匹配复杂事件和超时复杂事件的提取和处理
        SingleOutputStreamOperator<IceMarkerResult> resultStream = patternStream
                .select(orderTimeoutTag, new IceTimeoutSelect(), new IceCleanSelect());

        resultStream.print("cleaned normally");
        resultStream.getSideOutput(orderTimeoutTag).print("timeout");

        env.execute("clean timeout detect job");

    }

    // 实现自定义的超时事件处理函数
    public static class IceTimeoutSelect implements PatternTimeoutFunction<IceMarker, IceMarkerResult>{
        @Override
        public IceMarkerResult timeout(Map<String, List<IceMarker>> pattern, long timeoutTimestamp) throws Exception {
            Long timeoutIceId = pattern.get("begin").iterator().next().getIceId();
            return new IceMarkerResult(timeoutIceId, "timeout " + timeoutTimestamp);
        }
    }

    // 实现自定义的正常匹配事件处理函数
    public static class IceCleanSelect implements PatternSelectFunction<IceMarker, IceMarkerResult>{
        @Override
        public IceMarkerResult select(Map<String, List<IceMarker>> pattern) throws Exception {
            Long cleanIceId = pattern.get("next").iterator().next().getIceId();
            return new IceMarkerResult(cleanIceId, "清除成功！");
        }
    }
}
