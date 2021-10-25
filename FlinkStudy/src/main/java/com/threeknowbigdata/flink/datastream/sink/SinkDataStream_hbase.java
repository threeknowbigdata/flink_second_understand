package com.threeknowbigdata.flink.datastream.sink;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

/**
 * 类描述：
 *
 * @ClassName SinkDataStream_hbase
 * @Description:
 * @Author: 土哥
 * @Date: 2021/10/9 上午11:19
 */

/**
 *
 * kafka数据
 *
 * json格式
 *
 * 数据说明:数据说明包括，操作类型：增删改，根据数据的不同通过flink实时写入hbase，将表还原，
 * 其中delete操作类型根据生产要求，不能删除数据，并记录删除操作，before为操作前的数据，after为操作后的数据
 *
 * flink读取之后写到hbase
 */
public class SinkDataStream_hbase {
    public static void main(String[] args) {
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();

        senv.setParallelism(8);
        senv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        senv.enableCheckpointing(5000);

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

    }
}
