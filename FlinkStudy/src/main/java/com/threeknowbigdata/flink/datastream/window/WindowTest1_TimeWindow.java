package com.threeknowbigdata.flink.datastream.window;

import com.threeknowbigdata.flink.datastream.entity.Phone;
import com.threeknowbigdata.flink.datastream.entity.SensorReading;
import com.threeknowbigdata.flink.datastream.entity.Student;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;
import java.util.stream.Stream;

/**
 * 类描述：
 *
 * @ClassName WindowTest1_TimeWindow
 * @Description:
 * @Author: 土哥
 * @Date: 2021/8/31 下午5:01
 */
public class WindowTest1_TimeWindow {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();

        // socket文本流
       // DataStream<String> inputStream = senv.socketTextStream("192.168.244.129", 7777);
        DataStreamSource<String> inputStream = senv.readTextFile("data/senser.txt");

        DataStream<SensorReading> mapDs = inputStream.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String s) throws Exception {
                String[] split = s.split(",");
                return new SensorReading(split[0], new Long(split[1]), new Double(split[2]));
            }
        });
        // 开窗测试
        // 1. 增量聚合函数
        WindowedStream<SensorReading, Tuple, TimeWindow> idDs = mapDs.keyBy("id").window(TumblingProcessingTimeWindows.of(Time.seconds(15)));
        SingleOutputStreamOperator<Integer> aggregate = idDs.aggregate(new AggregateFunction<SensorReading, Integer, Integer>() {
            @Override
            public Integer createAccumulator() {
                return 0;
            }

            @Override
            public Integer add(SensorReading value, Integer accumulator) {
                return accumulator+1;
            }

            @Override
            public Integer getResult(Integer accumulator) {
                return accumulator;
            }

            @Override
            public Integer merge(Integer a, Integer b) {
                return a + b;
            }
        });
        aggregate.print();
        senv.execute();
        }
}
