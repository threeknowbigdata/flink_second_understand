package com.threeknowbigdata.flink.datastream.transform;

import com.threeknowbigdata.flink.hotitems_analysis.HotItems;
import com.threeknowbigdata.flink.hotitems_analysis.beans.ItemViewCount;
import com.threeknowbigdata.flink.networkflow_analysis.beans.UserBehavior;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.util.Collector;

import java.util.Properties;

/**
 * 类描述：
 *
 * @ClassName Transfor_map
 * @Description:
 * @Author: 土哥
 * @Date: 2021/9/27 下午9:29
 */
public class Transform_keyby {
    public static void main(String[] args) {
        //1.准备环境
        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        sEnv.setParallelism(3);
        //2.读取数据
        DataStreamSource<String> stream = sEnv.socketTextStream("192.168.244.129", 9999);
        //3.处理数据
        SingleOutputStreamOperator<Tuple2<String, Integer>> flatMapDs =
                stream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {

            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] line = s.split(" ");
                for (String word : line) {
                    collector.collect(new Tuple2<>(word, 1));
                }
            }
        });
        KeyedStream<Tuple2<String, Integer>, Tuple> keyDs = flatMapDs.keyBy(0);
    }
}
