package com.threeknowbigdata.flink.datastream;


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

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.util.Properties;

/**
 * 类描述：
 *
 * @ClassName WordCount_kafka
 * @Description:
 * @Author: 土哥
 * @Date: 2021/10/13 下午6:53
 */
public class WordCount_kafka {
    public static void main(String[] args) throws Exception {
        //Todo 1.准备环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.enableCheckpointing(5000);
        env.setParallelism(4);

        //Todo 2.准备kafka连接参数
        Properties prop = new Properties();
        prop.setProperty("bootstrap.servers", "192.168.244.129:9092");
        prop.setProperty("group.id", "consumer-group");
        prop.setProperty("auto.offset.reset", "earliest");
        String topic = "adClickLog";

        //Todo 3.创建kafka数据源
        FlinkKafkaConsumer<String> flinkKafkaConsumer = (FlinkKafkaConsumer<String>)
                new FlinkKafkaConsumer<String>(topic,new SimpleStringSchema(),prop);
        flinkKafkaConsumer.setStartFromEarliest();
        //Todo 4.指定消费者参数
        flinkKafkaConsumer.setCommitOffsetsOnCheckpoints(true);//默认为true
        //Todo 从最早的数据开始消费
        flinkKafkaConsumer.setStartFromEarliest();

        // 1. 从文件中读取数据
        DataStreamSource<String> stringDataStreamSource = env.addSource(flinkKafkaConsumer);
        //3、将接收到的数据转为单词元祖(执行转换操作)
        DataStream<Tuple2<String, Integer>> wordDatastream = stringDataStreamSource
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {

                    @Override
                    public void flatMap(
                            String value,
                            Collector<Tuple2<String, Integer>> out) throws Exception {
                        String[] line = value.split(",");
                        for (String word : line) {
                            out.collect(new Tuple2<>(word, 1));
                        }

                    }
                }).setParallelism(4);

        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = wordDatastream
                .keyBy(0);
        DataStream<Tuple2<String, Integer>> sinkDtastream = keyedStream.sum(1).setParallelism(4);

        //4、sink操作
        sinkDtastream.print();
        env.execute("submit job");
    }
}
