package com.threeknowbigdata.flink.table;


import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;


/**
 * 类描述：
 *
 * @ClassName KafkaToKafka
 * @Description:
 * @Author: 土哥
 * @Date: 2021/9/14 上午11:36
 */
public class KafkaToKafka {
    public static void main(String[] args) {
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();
        senv.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        senv.setParallelism(3);
        //EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(senv);

        tEnv.executeSql("create table kafka_source(" +
                "name string," +
                "age string," +
                "sex string" +
                ")with(" +
                "'connector' = 'kafka'," +
                "'topic' = 'test1'," +
                "'properties.bootstrap.servers' = '192.168.244.129:9092'," +
                "'properties.group.id' = 'testGroup'," +
                "'scan.startup.mode' = 'earliest-offset'," +
                "'format' = 'csv'" +
                ")");
        tEnv.executeSql("create table kafka_sink(" +
                "name string," +
                "age string," +
                "sex string" +
                ")with(" +
                "'connector' = 'kafka'," +
                "'topic' = 'test2'," +
                "'properties.bootstrap.servers' = '192.168.244.129:9092'," +
                "'properties.group.id' = 'testGroup'," +
                "'scan.startup.mode' = 'earliest-offset'," +
                "'format' = 'csv'" +
                ")");
        tEnv.executeSql("insert into kafka_sink select * from kafka_source");
    }
}
