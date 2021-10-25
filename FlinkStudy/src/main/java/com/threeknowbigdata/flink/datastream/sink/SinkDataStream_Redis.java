package com.threeknowbigdata.flink.datastream.sink;

import com.threeknowbigdata.flink.datastream.entity.Teacher;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

/**
 * 类描述：
 *
 * @ClassName SinkDataStream_Redis
 * @Description:
 * @Author: lyz
 * @Date: 2021/10/12 上午11:10
 */
public class SinkDataStream_Redis {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 从文件读取数据
        DataStream<String> inputStream = env.readTextFile("/home/workspace/javaspace/FlinkStudy/src/main/java/com/threeknowbigdata/flink/hotitems_analysis/data/teacher.txt");

        // 转换成Teacher类型
        DataStream<Teacher> dataStream = inputStream.map(line -> {
            String[] fields = line.split(",");
            return new Teacher(fields[0], new Integer(fields[1]));
        });

        // 定义jedis连接配置
        FlinkJedisPoolConfig config = new FlinkJedisPoolConfig.Builder()
                .setHost("192.168.244.129")
                .setPort(6379)
                .build();

        dataStream.addSink( new RedisSink<>(config, new MyRedisMapper()));

        env.execute();
    }

    // 自定义RedisMapper
    public static class MyRedisMapper implements RedisMapper<Teacher> {
        // 定义保存数据到redis的命令，存成Hash表，hset teacher id
        @Override
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.HSET, "teacher");
        }

        @Override
        public String getKeyFromData(Teacher data) {
            return data.getId();
        }

        @Override
        public String getValueFromData(Teacher data) {
            String s = String.valueOf(data.getAge());
            return s;
        }
    }
}
