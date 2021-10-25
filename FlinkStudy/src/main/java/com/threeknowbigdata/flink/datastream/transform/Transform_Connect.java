package com.threeknowbigdata.flink.datastream.transform;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 *
 * @ClassName Transform_Connect
 * @Description:
 * @Author: 土哥
 * @Date: 2021/9/27 下午10:27
 */
public class Transform_Connect {
    public static void main(String[] args) throws Exception {
        //1.准备环境
        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.获取数据
        DataStreamSource<Long> longDs = sEnv.addSource(new MyLongSourceJava());
        DataStreamSource<String> strDs = sEnv.addSource(new MyStringSourceJava());
        //3.处理数据,使用connect
        ConnectedStreams<Long, String> connectDs = longDs.connect(strDs);

        SingleOutputStreamOperator<String> map = connectDs.map(new CoMapFunction<Long, String,String>() {

            @Override
            public String map1(Long aLong) throws Exception {
                return "Long:" +aLong;
            }

            @Override
            public String map2(String s) throws Exception {
                return "String:" +s;
            }
        });
        map.print();

        sEnv.execute();



    }
}



/**
 * 自定义source实现从1开始产生递增字符串
 */
class MyStringSourceJava implements SourceFunction<String> {
        Long count = 1L;
        Boolean isRunning = true;
    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        while (isRunning){
            ctx.collect("str_" + count);
            count++;
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
/**
 * 自定义source实现从1开始产生递增数字
 */
class MyLongSourceJava implements SourceFunction<Long> {
    Long count = 1L;
    Boolean isRunning = true;
    @Override
    public void run(SourceContext<Long> ctx) throws Exception {
        while (isRunning){
            ctx.collect(count);
            count++;
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}