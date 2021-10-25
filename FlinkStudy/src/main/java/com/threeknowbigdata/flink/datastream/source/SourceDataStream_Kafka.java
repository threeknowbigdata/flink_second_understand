package com.threeknowbigdata.flink.datastream.source;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 类描述：
 *
 * @ClassName SourceDataStream_Kafka
 * @Description:
 * @Author: 土哥
 * @Date: 2021/8/31 下午9:11
 */
public class SourceDataStream_Kafka {
    public static void main(String[] args) throws Exception {
        //Todo 1.准备环境
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();
        senv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        senv.enableCheckpointing(5000);
        senv.setParallelism(4);

        //Todo 2.准备kafka连接参数
        Properties prop = new Properties();
        prop.setProperty("bootstrap.servers", "192.168.244.129:9092");
        prop.setProperty("group.id", "consumer-group");
        prop.setProperty("auto.offset.reset", "earliest");
        String topic = "kafka_data_test";

        //Todo 3.创建kafka数据源
        FlinkKafkaConsumer<String> flinkKafkaConsumer = (FlinkKafkaConsumer<String>)
                new FlinkKafkaConsumer<String>(topic,new SimpleStringSchema(),prop);
        flinkKafkaConsumer.setStartFromEarliest();
        //Todo 4.指定消费者参数
        flinkKafkaConsumer.setCommitOffsetsOnCheckpoints(true);//默认为true
        //Todo 从最早的数据开始消费
        flinkKafkaConsumer.setStartFromEarliest();
        //默认值，从当前消费组记录的偏移量接着上次的开始消费,如果没有找到，
        //flinkKafkaConsumer.setStartFromGroupOffsets();//从消费者组开始
        //flinkKafkaConsumer.setStartFromLatest//从最新的数据开始消费
        //flinkKafkaConsumer.setStartFromTimestamp(1568908800000L)//根据指定的时间戳消费数据
        //还可以为每个分区指定消费者应该从哪里开始的确切偏移量
        //key是KafkaTopicPartition(topic，分区id),value是偏移量
/*        Map<KafkaTopicPartition, Long> specificStartOffsets = new HashMap<>();
        specificStartOffsets.put(new KafkaTopicPartition("kafka_data_test", 0), 23L);
        specificStartOffsets.put(new KafkaTopicPartition("kafka_data_test", 1), 31L);
        specificStartOffsets.put(new KafkaTopicPartition("kafka_data_test", 2), 43L);
        flinkKafkaConsumer.setStartFromSpecificOffsets(specificStartOffsets);*/
        //kafkaConsumerSource.setStartFromSpecificOffsets(offsets)//从指定的具体位置开始消费
        //Todo 5.从kafka数据源获取数据
        DataStreamSource<String> kafkaDs  = senv.addSource(flinkKafkaConsumer);
        //Todo 6.处理输出数据
        kafkaDs.print();
        //Todo 7.启动执行
        senv.execute();
    }
}
