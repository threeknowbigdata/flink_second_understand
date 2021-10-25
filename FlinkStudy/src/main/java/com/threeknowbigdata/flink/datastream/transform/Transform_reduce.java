package com.threeknowbigdata.flink.datastream.transform;

import com.threeknowbigdata.flink.datastream.entity.Teacher;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * 类描述：
 *
 * @ClassName Transfor_map
 * @Description:
 * @Author: 土哥
 * @Date: 2021/9/27 下午9:29
 */
public class Transform_reduce {
    public static void main(String[] args) throws Exception {
        //1.准备环境
        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        sEnv.setParallelism(3);
        //2.读取数据
        DataStreamSource<String> stream = sEnv.readTextFile("data/teacher.txt");
        //3.转为teacher类型

        DataStream<Teacher> mapDs
                = stream.map(new MapFunction<String, Teacher>() {
            @Override
            public Teacher map(String s) throws Exception {
                String[] line = s.split(",");
                return new Teacher(line[0], new Integer(line[1]));
            }
        });
        KeyedStream<Teacher, Object> keyDs = mapDs.keyBy(data -> data.getId());
        DataStream<Teacher> reduce = keyDs.reduce(new ReduceFunction<Teacher>() {
            @Override
            public Teacher reduce(Teacher t1, Teacher t2) throws Exception {
                return new Teacher(
                        t1.getId(), Math.min(t1.getAge(), t2.getAge())
                );
            }
        });

        reduce.print();
        sEnv.execute();
    }
}
