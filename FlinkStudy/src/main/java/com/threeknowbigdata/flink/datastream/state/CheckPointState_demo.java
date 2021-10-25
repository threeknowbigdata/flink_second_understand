package com.threeknowbigdata.flink.datastream.state;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * 类描述：
 *
 * @ClassName CheckPointState_demo
 * @Description:
 * @Author: 土哥
 * @Date: 2021/10/9 下午1:57
 */
public class CheckPointState_demo {
    private static Logger logger = LoggerFactory.getLogger(CheckPointState_demo.class);

    public static void main(String[] args) {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置并行度
        env.setParallelism(4);
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        //设置checkpoint   1分钟
        env.enableCheckpointing(60000);

        // 设置模式为exactly-once （这是默认值）
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //重启策略
        env.setRestartStrategy(RestartStrategies.noRestart());
        // 确保检查点之间有至少500 ms的间隔【checkpoint最小间隔】
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        // 检查点必须在一分钟内完成，或者被丢弃【checkpoint的超时时间】
        env.getCheckpointConfig().setCheckpointTimeout(10000);
        // 同一时间只允许进行一个检查点
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);

        //设置statebackend

        env.setStateBackend(new FsStateBackend("hdfs://node1.hadoop:9000/flink/checkpoint", true));

        // 表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的Checkpoint【详细解释见备注】
        //ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION:表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的Checkpoint
        //ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION: 表示一旦Flink处理程序被cancel后，会删除Checkpoint数据，只有job执行失败的时候才会保存checkpoint
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);





        //todo 获取kafka的配置属性
        args = new String[]{"--input-topic", "topn_test", "--bootstrap.servers", "node2.hadoop:9092,node3.hadoop:9092",
                "--zookeeper.connect", "node1.hadoop:2181,node2.hadoop:2181,node3.hadoop:2181", "--group.id", "c1"};

        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        Properties sendPros = parameterTool.getProperties();
        Properties pros = parameterTool.getProperties();

        //todo 指定输入数据为kafka topic
        DataStream<String> kafkaDstream = env.addSource(new FlinkKafkaConsumer<String>(
                pros.getProperty("input-topic"),
                new SimpleStringSchema(),
                pros).setStartFromGroupOffsets()

        );
//        kafkaDstream.print();
//        DataStream<String> mapDstream = kafkaDstream.keyBy(x->x).map(new stateMap());
//
//        mapDstream.print();
//        try {
//            env.execute("startExecute");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}

