package com.threeknowbigdata.flink.datastream.source;

import com.threeknowbigdata.flink.datastream.entity.SensorReading;
import com.threeknowbigdata.flink.datastream.entity.Student;
import com.threeknowbigdata.flink.datastream.entity.Teacher;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.HashMap;
import java.util.Random;

/**
 * 类描述：
 *
 * @ClassName SourceDataStream_UDF
 * @Description:
 * @Author: 土哥
 * @Date: 2021/8/31 下午3:17
 */
public class SourceDataStream_UDF {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Teacher> dataStream = senv.addSource(new MyTeacherSource());

        // 打印输出
        dataStream.print();

        senv.execute();
    }

    // 实现自定义的SourceFunction
    public static class MyTeacherSource implements SourceFunction<Teacher>{

        // 定义一个标识位，用来控制数据的产生
        private boolean running = true;

        @Override
        public void run(SourceContext<Teacher> sourceContext) throws Exception {
            // 定义一个随机数发生器
            Random random = new Random();

            // 设置10个老师的年龄
            HashMap<String, Integer> teacherMap = new HashMap<>();
            for( int i = 0; i < 10; i++ ){
                teacherMap.put("tId" + (i+1), 20+random.nextInt(11));
            }

            while (running){
                for( String tId: teacherMap.keySet() ){
                    // 在当前年龄上+1
                    int newAge = teacherMap.get(tId) + 1;
                    teacherMap.put(tId, newAge);
                    sourceContext.collect(new Teacher(tId,  newAge));
                }
                // 控制输出频率
                Thread.sleep(1000L);
            }
        }

        @Override
        public void cancel() {
            running = false;
        }
    }
}
