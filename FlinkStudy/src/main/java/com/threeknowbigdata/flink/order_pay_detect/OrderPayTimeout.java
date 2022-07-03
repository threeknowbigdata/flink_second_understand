package com.threeknowbigdata.flink.order_pay_detect;

import com.threeknowbigdata.flink.order_pay_detect.bean.OrderResult;
import com.threeknowbigdata.flink.order_pay_detect.bean.OrderEvent;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.util.OutputTag;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.PatternTimeoutFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.windowing.time.Time;
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
public class OrderPayTimeout {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);

        // 读取数据并转换成POJO类型
        DataStreamSource<String> stringDataStreamSource = env.readTextFile("D:\\flink_second_understand\\FlinkStudy\\src\\main\\java\\com\\threeknowbigdata\\flink\\order_pay_detect\\data\\OrderLog.csv");
        KeyedStream<OrderEvent, Long> orderEventStream = stringDataStreamSource.map(new MapFunction<String, OrderEvent>() {
            @Override
            public OrderEvent map(String line) throws Exception {
                String[] fields = line.split(",");
                return new OrderEvent(new Long(fields[0]), fields[1], fields[2], new Long(fields[3]));
            }
        }).assignTimestampsAndWatermarks(new AscendingTimestampExtractor<OrderEvent>() {
            @Override
            public long extractAscendingTimestamp(OrderEvent element) {
                return element.getTimestamp() * 1000L;
            }
        }).keyBy(OrderEvent::getOrderId);

        System.out.println(orderEventStream);

        // 1. 定义一个带时间限制的模式
        Pattern<OrderEvent, OrderEvent> orderPayPattern = Pattern
                .<OrderEvent>begin("create").where(new SimpleCondition<OrderEvent>() {
                    @Override
                    public boolean filter(OrderEvent value) throws Exception {
                        return "create".equals(value.getEventType());
                    }
                })
                .followedBy("pay").where(new SimpleCondition<OrderEvent>() {
                    @Override
                    public boolean filter(OrderEvent value) throws Exception {
                        return "pay".equals(value.getEventType());
                    }
                }).notOccur(Time.minutes(15));

        // 2. 定义侧输出流标签，用来表示超时事件
        OutputTag<OrderResult> orderTimeoutTag = new OutputTag<OrderResult>("order-timeout"){};



        // 3. 将pattern应用到输入数据流上，得到pattern stream
        PatternStream<OrderEvent> patternStream = CEP.pattern(orderEventStream, orderPayPattern);

        // 4. 调用select方法，实现对匹配复杂事件和超时复杂事件的提取和处理
        SingleOutputStreamOperator<OrderResult> resultStream = patternStream
                .select(orderTimeoutTag, new OrderTimeoutSelect(), new OrderPaySelect());

        resultStream.print("payed normally");
        resultStream.getSideOutput(orderTimeoutTag).print("timeout");

        env.execute("order timeout detect job");

    }

    // 实现自定义的超时事件处理函数
    public static class OrderTimeoutSelect implements PatternTimeoutFunction<OrderEvent, OrderResult>{
        @Override
        public OrderResult timeout(Map<String, List<OrderEvent>> pattern, long timeoutTimestamp) throws Exception {
            Long timeoutOrderId = pattern.get("create").iterator().next().getOrderId();
            return new OrderResult(timeoutOrderId, "timeout " + timeoutTimestamp);
        }
    }

    // 实现自定义的正常匹配事件处理函数
    public static class OrderPaySelect implements PatternSelectFunction<OrderEvent, OrderResult>{
        @Override
        public OrderResult select(Map<String, List<OrderEvent>> pattern) throws Exception {
            Long payedOrderId = pattern.get("pay").iterator().next().getOrderId();
            return new OrderResult(payedOrderId, "payed");
        }
    }
}
