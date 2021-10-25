package com.threeknowbigdata.flink.dataset;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * 类描述：
 *
 * @ClassName FlinkJob
 * @Description:
 * @Author: 土哥
 * @Date: 2021/8/13 10:16
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        //创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);
        //2\构建source 输入源
        DataSource<String> txDataSet = env.fromElements("sdk dakjsd dajk ad da as as ad kjhdi eqiuy  qieuy qeiy ");
       // DataStreamSource<String> txDataStream = executionEnvironment.socketTextStream(
       //         "192.168.244.129",
       //         9999);

        //3、将接收到的数据转为单词元祖(执行转换操作)
        FlatMapOperator<String, Tuple2<String, Integer>> wordDataSet = txDataSet
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {

                    @Override
                    public void flatMap(
                            String value,
                            Collector<Tuple2<String, Integer>> out) throws Exception {
                        String[] line = value.split(" ");
                        for (String word : line) {
                            out.collect(new Tuple2<>(word, 1));
                        }

                    }
                });
        AggregateOperator<Tuple2<String, Integer>> sinkDataSet = wordDataSet.groupBy(0).sum(1).setParallelism(1);

        //4、sink操作
        sinkDataSet.print();
        //env.execute();

    }
}
