package com.threeknowbigdata.flink.datastream.kafka;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.flink.streaming.api.TimeCharacteristic;

import java.util.Properties;
/**
 * topic = "kafka_source_topic"
 * 数据为 data/lightgbm_test.csv,将该数据发送到 kafka 的 kafka_source_topic中
 */

/**
 * @Auther lyz
 * @Date 2020/7/29
 */
public class KafkaAsProductTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();
        senv.enableCheckpointing(5000);
        // 非常关键，一定要设置启动检查点！！
        senv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        Properties prop = new Properties();
        //kafka 集群地址
        prop.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.244.129:9092");
        //消费者组
        prop.setProperty(ConsumerConfig.GROUP_ID_CONFIG,"flink");
        //动态分区检测
        prop.setProperty("flink.partition-discovery.interval-millis","5000");
        // 设置默认消费的偏移量起始值
        prop.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        String topic = "kafka_data_test";

        // 3 获取一个kafkaconsumer对象
        FlinkKafkaConsumer<String> flinkKafkaConsumer = (FlinkKafkaConsumer<String>) new FlinkKafkaConsumer<String>(topic, new SimpleStringSchema(), prop).setStartFromLatest();

        DataStreamSource<String> stringDataStreamSource = senv.addSource(flinkKafkaConsumer);

        stringDataStreamSource.print();
        senv.execute("Kafka-Flink Test");
    }

}
