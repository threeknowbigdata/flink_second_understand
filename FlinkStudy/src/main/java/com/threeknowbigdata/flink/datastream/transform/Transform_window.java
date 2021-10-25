package com.threeknowbigdata.flink.datastream.transform;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * 类描述：
 *
 * @ClassName Transfor_map
 * @Description:
 * @Author: 土哥
 * @Date: 2021/9/27 下午9:29
 */
public class Transform_window {
    public static void main(String[] args) throws Exception {
        //1.准备环境
        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        sEnv.setParallelism(1);
        //2.读取数据
        DataStreamSource<String> stream = sEnv.socketTextStream("192.168.244.129", 9999);
        //3.处理数据
        SingleOutputStreamOperator<Tuple2<String, Integer>> flatMapDs =
                stream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {

            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] line = s.split(" ");
                for (String word : line) {
                    out.collect(new Tuple2<>(word, 1));
                }
            }
        });
        KeyedStream<Tuple2<String, Integer>, Tuple> keyDs = flatMapDs.keyBy(0);
        WindowedStream<Tuple2<String, Integer>, Tuple, TimeWindow> windowDs =
                keyDs.window(TumblingProcessingTimeWindows.of(Time.seconds(5)));
        //todo 4.sink操作
        windowDs.sum(1).print();
        sEnv.execute();
    }
}
