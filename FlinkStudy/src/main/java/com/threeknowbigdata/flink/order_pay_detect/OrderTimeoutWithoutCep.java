package com.threeknowbigdata.flink.order_pay_detect;

import com.threeknowbigdata.flink.order_pay_detect.bean.OrderEvent;
import com.threeknowbigdata.flink.order_pay_detect.bean.OrderResult;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.net.URL;

/**
 * 类描述：
 *
 * @ClassName OrderTimeoutWithoutCep
 * @Description:
 * @Author: 土哥
 * @Date: 2021/9/1 下午2:49
 */
public class OrderTimeoutWithoutCep {
    // 定义超时事件的侧输出流标签
    private final static OutputTag<OrderResult> orderTimeoutTag = new OutputTag<OrderResult>("order-timeout"){};

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);

        // 读取数据并转换成POJO类型
        DataStreamSource<String> stringDataStreamSource = env.readTextFile("D:\\flink_second_understand\\FlinkStudy\\src\\main\\java\\com\\threeknowbigdata\\flink\\order_pay_detect\\data\\OrderLog.csv");
        DataStream<OrderEvent> orderEventStream = stringDataStreamSource.map(new MapFunction<String, OrderEvent>() {
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
        });

        // 自定义处理函数，主流输出正常匹配订单事件，侧输出流输出超时报警事件
        SingleOutputStreamOperator<OrderResult> resultStream = orderEventStream
                .keyBy(OrderEvent::getOrderId)
                .process(new OrderPayMatchDetect());

        resultStream.print("payed normally");
        resultStream.getSideOutput(orderTimeoutTag).print("timeout");

        env.execute("order timeout detect without cep job");
    }

    // 实现自定义KeyedProcessFunction
    public static class OrderPayMatchDetect extends KeyedProcessFunction<Long, OrderEvent, OrderResult>{
        // 定义状态，保存之前点单是否已经来过create、pay的事件
        ValueState<Boolean> isPayedState;
        ValueState<Boolean> isCreatedState;
        // 定义状态，保存定时器时间戳
        ValueState<Long> timerTsState;

        @Override
        public void open(Configuration parameters) throws Exception {
            isPayedState = getRuntimeContext().getState(new ValueStateDescriptor<Boolean>("is-payed", Boolean.class, false));
            isCreatedState = getRuntimeContext().getState(new ValueStateDescriptor<Boolean>("is-created", Boolean.class, false));
            timerTsState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("timer-ts", Long.class));
        }

        @Override
        public void processElement(OrderEvent value, Context ctx, Collector<OrderResult> out) throws Exception {
            // 先获取当前状态
            Boolean isPayed = isPayedState.value();
            Boolean isCreated = isCreatedState.value();
            Long timerTs = timerTsState.value();

            // 判断当前事件类型
            if( "create".equals(value.getEventType()) ){
                // 1. 如果来的是create，要判断是否支付过
                if( isPayed ){
                    // 1.1 如果已经正常支付，输出正常匹配结果
                    out.collect(new OrderResult(value.getOrderId(), "payed successfully"));
                    // 清空状态，删除定时器
                    isCreatedState.clear();
                    isPayedState.clear();
                    timerTsState.clear();
                    ctx.timerService().deleteEventTimeTimer(timerTs);
                } else {
                    // 1.2 如果没有支付过，注册15分钟后的定时器，开始等待支付事件
                    Long ts = ( value.getTimestamp() + 15 * 60 ) * 1000L;
                    ctx.timerService().registerEventTimeTimer(ts);
                    // 更新状态
                    timerTsState.update(ts);
                    isCreatedState.update(true);
                }
            } else if( "pay".equals(value.getEventType()) ){
                // 2. 如果来的是pay，要判断是否有下单事件来过
                if( isCreated ){
                    // 2.1 已经有过下单事件，要继续判断支付的时间戳是否超过15分钟
                    if( value.getTimestamp() * 1000L < timerTs ){
                        // 2.1.1 在15分钟内，没有超时，正常匹配输出
                        out.collect(new OrderResult(value.getOrderId(), "payed successfully"));
                    } else {
                        // 2.1.2 已经超时，输出侧输出流报警
                        ctx.output(orderTimeoutTag, new OrderResult(value.getOrderId(), "payed but already timeout"));
                    }
                    // 统一清空状态
                    isCreatedState.clear();
                    isPayedState.clear();
                    timerTsState.clear();
                    ctx.timerService().deleteEventTimeTimer(timerTs);
                } else {
                    // 2.2 没有下单事件，乱序，注册一个定时器，等待下单事件
                    ctx.timerService().registerEventTimeTimer( value.getTimestamp() * 1000L);
                    // 更新状态
                    timerTsState.update(value.getTimestamp() * 1000L);
                    isPayedState.update(true);
                }
            }
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<OrderResult> out) throws Exception {
            // 定时器触发，说明一定有一个事件没来
            if( isPayedState.value() ){
                // 如果pay来了，说明create没来
                ctx.output(orderTimeoutTag, new OrderResult(ctx.getCurrentKey(), "payed but not found created log"));
            }else {
                // 如果pay没来，支付超时
                ctx.output(orderTimeoutTag, new OrderResult(ctx.getCurrentKey(), "timeout"));
            }
            // 清空状态
            isCreatedState.clear();
            isPayedState.clear();
            timerTsState.clear();
        }
    }
}

