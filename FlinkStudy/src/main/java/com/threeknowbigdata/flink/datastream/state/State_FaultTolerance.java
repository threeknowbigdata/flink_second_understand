package com.threeknowbigdata.flink.datastream.state;

import com.threeknowbigdata.flink.datastream.entity.SensorReading;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.contrib.streaming.state.EmbeddedRocksDBStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 *
 * @ClassName State_FaultTolerance
 * @Description:
 * @Author: liyaozhou
 * @Date: 2022/3/15 13:48
 */

public class State_FaultTolerance {

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        configuration.setInteger("state.checkpoints.num-retained", 10);
        configuration.setBoolean("state.backend.incremental", true);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
//        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //Todo 1.准备 Checkpoint 和 状态后端的参数
        EmbeddedRocksDBStateBackend embeddedRocksDBStateBackend = new EmbeddedRocksDBStateBackend();
        //1 设置并行度
        env.setParallelism(4);
//      env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        //2 设置 checkpoint 1分钟，设置模式为 exactly-once （这是默认值）
        env.enableCheckpointing(TimeUnit.SECONDS.toMillis(1), CheckpointingMode.EXACTLY_ONCE);
        //3 设置重启策略，固定延迟重启策略
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 10000L));

        //4 设置存储的 checkpoint 路径
        //env.getCheckpointConfig().setCheckpointStorage("file:///E:\\xxx");
        env.getCheckpointConfig().setCheckpointStorage("hdfs://192.168.244.131:9000/flink-study/ck");
        //5 两次 checkpoint 的间隔时间设置至少 2 分钟
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(TimeUnit.SECONDS.toMillis(2));
        //6 同一时间只允许进行一个检查点
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //7 设置最大可容忍的连续失败次数为 3 次
        env.getCheckpointConfig().setTolerableCheckpointFailureNumber(3);
        //8 checkpoint 必须在 1分钟内结束，否则被丢弃，默认是 10 分钟
        env.getCheckpointConfig().setCheckpointTimeout(TimeUnit.MINUTES.toMillis(1));
        //9 当 Flink 任务取消时，保留外部保存的 checkpoint 信息
        env.getCheckpointConfig().enableExternalizedCheckpoints
                (CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //10 设置状态后端为 RocksDB
        env.setStateBackend(embeddedRocksDBStateBackend);


        //Todo 2.准备 kafka 连接参数
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("192.168.244.131:9092")
                .setTopics("SourceSensorReading")
                .setGroupId("consumer-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        //Todo 3.准备 Source 端
        DataStreamSource<String> kafka_source = env.fromSource
                (source, WatermarkStrategy.noWatermarks(), "Kafka Source");

        // Todo 4. 准备 Transformation

        // 转换成SensorReading类型
        DataStream<SensorReading> dataStream = kafka_source.map(line -> {
            String[] fields = line.split(",");
            return new SensorReading(fields[0], new Long(fields[1]), new Double(fields[2]));
        });

        // 定义一个flatmap操作，检测温度跳变，输出报警
        SingleOutputStreamOperator<Tuple3<String, Double, Double>> resultStream = dataStream.keyBy("id")
                .flatMap(new TempChangeWarning(10.0));


        //Todo 5.准备 sink 连接参数
//        KafkaSink<String> sinkOutput = KafkaSink.<String>builder()
//                .setBootstrapServers("192.168.244.131:9092")
//                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
//                        .setTopic("SinkSensorReading")
//                        .setValueSerializationSchema(new SimpleStringSchema())
//                        .build()
//                ).build();
        resultStream.print().setParallelism(1);
        env.execute("");

    }

    // 实现自定义函数类
    public static class TempChangeWarning extends RichFlatMapFunction<SensorReading, Tuple3<String, Double, Double>> {
        // 私有属性，温度跳变阈值
        private Double threshold;

        public TempChangeWarning(Double threshold) {
            this.threshold = threshold;
        }

        // 定义状态，保存上一次的温度值
        private ValueState<Double> lastTempState;

        @Override
        public void open(Configuration parameters) throws Exception {
            lastTempState = getRuntimeContext().getState(new ValueStateDescriptor<Double>("last-temp", Double.class));
        }

        @Override
        public void flatMap(SensorReading value, Collector<Tuple3<String, Double, Double>> out) throws Exception {
            // 获取状态
            Double lastTemp = lastTempState.value();

            // 如果状态不为 null，那么就判断两次温度差值
            if( lastTemp != null ){
                Double diff = Math.abs( value.getTemperature() - lastTemp );
                if( diff >= threshold )
                    out.collect(new Tuple3<>(value.getId(), lastTemp, value.getTemperature()));
            }

            // 更新状态
            lastTempState.update(value.getTemperature());
        }

        @Override
        public void close() throws Exception {
            lastTempState.clear();
        }
    }
}
