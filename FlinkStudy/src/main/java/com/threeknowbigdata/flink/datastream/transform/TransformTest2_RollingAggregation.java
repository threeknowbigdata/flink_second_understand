package com.threeknowbigdata.flink.datastream.transform;

import com.threeknowbigdata.flink.datastream.entity.Student;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
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
public class TransformTest2_RollingAggregation {
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

        //fen zu
 //       KeyedStream<Student, Tuple> sexDatastream = mapDs.keyBy("sex");
        KeyedStream<Student, String> sexDs = mapDs.keyBy(data -> data.getSex());

        //取当前最大年龄
 //       SingleOutputStreamOperator<Student> ageDs = sexDatastream.maxBy("age");
 //       ageDs.print();

        sexDs.print();

        senv.execute();

    }
}
