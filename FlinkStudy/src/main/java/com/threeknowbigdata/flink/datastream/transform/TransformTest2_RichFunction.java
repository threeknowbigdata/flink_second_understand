package com.threeknowbigdata.flink.datastream.transform;

import com.threeknowbigdata.flink.datastream.entity.Student;
import org.apache.flink.api.common.functions.*;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 类描述：
 *
 * @ClassName TransformTest2_RollingAggregation
 * @Description:
 * @Author: 土哥
 * @Date: 2021/8/31 下午3:55
 */
public class TransformTest2_RichFunction {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();
        senv.setParallelism(2);

        DataStreamSource<String> readDs = senv.readTextFile("data/student.csv");
        DataStream<Student> mapDs = readDs.map(new MapFunction<String, Student>() {
            @Override
            public Student map(String s) throws Exception {
                String[] split = s.split(",");
                return new Student(split[0], new Integer(split[1]), split[2]);
            }
        });

        SingleOutputStreamOperator<Tuple2<String, Integer>> mapDs1 = mapDs.map(new MyMapper());

        mapDs1.print();
        senv.execute();

    }


    // 实现自定义富函数类
    public static class MyMapper extends RichMapFunction<Student, Tuple2<String, Integer>> {


        @Override
        public Tuple2<String, Integer> map(Student student) throws Exception {
            return new Tuple2<>(student.getName(),student.getAge());
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            // 初始化工作，一般是定义状态，或者建立数据库连接
            System.out.println("open");
        }

        @Override
        public void close() throws Exception {
            // 一般是关闭连接和清空状态的收尾操作
            System.out.println("close");
        }
    }
}
