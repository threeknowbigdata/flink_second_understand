package com.threeknowbigdata.flink.datastream;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
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
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        executionEnvironment.setParallelism(1);
        //2\构建source 输入源
        //DataStreamSource<String> txDataStream = executionEnvironment.fromElements("sdk dakjsd dajk ad da as as ad kjhdi eqiuy  qieuy qeiy ");
        DataStreamSource<String> txDataStream = executionEnvironment.socketTextStream(
                "192.168.244.129",
                9999);

        //3、将接收到的数据转为单词元祖(执行转换操作)
        DataStream<Tuple2<String, Integer>> wordDatastream = txDataStream
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

        DataStream<Tuple2<String, Integer>> sinkDtastream = wordDatastream
                .keyBy(0)
                .sum(1)
                .setParallelism(1);
        //4、sink操作
        sinkDtastream.print();
        executionEnvironment.execute();

    }
}
