package com.threeknowbigdata.flink.datastream.transform;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
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
public class Transform_Filter {
    public static void main(String[] args) {
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();
        senv.setParallelism(2);

        DataStreamSource<Integer> dataSource = senv.fromElements(0,1,2,3,4,5);
        dataSource.filter(new FilterFunction<Integer>() {
            @Override
            public boolean filter(Integer value) throws Exception {
                return value != 0;
            }
        });
    }
}
