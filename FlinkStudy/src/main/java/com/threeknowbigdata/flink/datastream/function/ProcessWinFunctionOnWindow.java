package com.threeknowbigdata.flink.datastream.function;


import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

/**
 * 测试ProcessWinFunction
 *
 * @author dajiangtai
 * @create 2019-06-11-18:37
 */
public class ProcessWinFunctionOnWindow {

    public static void main(String[] args) throws Exception{
        //获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //读取数据
        DataStream<Tuple3<String,String,Long>> input = env.fromElements(ENGLISH);

        //求各班级英语成绩平均分
        DataStream<Double> avgScore = input.keyBy(0)
                .countWindow(2)
                .process(new MyProcessWindowFunction());
        avgScore.print();
        env.execute("TestProcessWinFunctionOnWindow");

    }


    public static class MyProcessWindowFunction extends ProcessWindowFunction<Tuple3<String,String,Long>,Double, Tuple, GlobalWindow>{

        //iterable 输入流中的元素类型集合
        @Override
        public void process(Tuple tuple, Context context, Iterable<Tuple3<String, String, Long>> iterable, Collector<Double> out) throws Exception {
            long sum = 0;
            long count = 0;
            for (Tuple3<String,String,Long> in :iterable){
                sum+=in.f2;
                count++;
            }
            out.collect((double)(sum/count));
        }
    }

    public static final Tuple3[] ENGLISH = new Tuple3[]{
            Tuple3.of("class1","张三",100L),
            Tuple3.of("class1","李四",78L),
            Tuple3.of("class1","王五",99L),
            Tuple3.of("class2","赵六",81L),
            Tuple3.of("class2","小七",59L),
            Tuple3.of("class2","小八",97L),
    };
}