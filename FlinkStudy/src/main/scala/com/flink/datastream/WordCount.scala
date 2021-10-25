package com.flink.datastream

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.api.scala.{AggregateDataSet, DataSet, ExecutionEnvironment, GroupedDataSet, createTypeInformation}
import org.apache.flink.core.fs.FileSystem
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment, WindowedStream}
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

/**
 * @ClassName Main
 * @Description:
 * @Author: 土哥
 * @Date: 2021/8/13 11:44
 */

object WordCount {
  def main(args: Array[String]): Unit = {

    /*
   1.	获得一个Stream execution environment，
   2.	加载/创建初始数据，
   3.	指定这些数据的转换，
   4.	指定将计算结果放在哪里，
   5.	触发程序执行
*/
    //1.	获得一个Stream execution environment，
    val sEnv = StreamExecutionEnvironment.getExecutionEnvironment
    // 设置并行度为1
    sEnv.setParallelism(1)

    //  2.	加载/创建初始数据，
    val sourceDs: DataStream[String] = sEnv.fromElements("Apache Flink is an open source platform for distributed stream and batch data processing",
      "Flink’s core is a streaming dataflow engine that provides data distribution")
    //思路： 对每行语句按照空格进行切分，切分之后组成单词（单词，1）tuple,按照单词进行分组，最后进行聚合计算
    //val txDataStream: DataStream[String] = sEnv.socketTextStream("192.168.244.129", 9999)
    // 3 指定这些数据的转换, transformation
    val wordDs: DataStream[String] = sourceDs.flatMap(_.split(" "))
    //(单词，1)
    var wordAndoneDs: DataStream[(String, Int)] = wordDs.map((_, 1))

    val keyDs: KeyedStream[(String, Int), Tuple] = wordAndoneDs.keyBy(0)

    var windowDs: WindowedStream[(String, Int), Tuple, TimeWindow] = keyDs.window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
    //聚合
    val aggDs: DataStream[(String, Int)] = windowDs.sum(1)
    //4 指定将计算结果放在哪里,存到HDFS中
    aggDs.print()
    aggDs.writeAsText("hdfs://192.168.244.129:9000/out",FileSystem.WriteMode.OVERWRITE)
    // 5 出发程序执行
    //sEnv.execute()
  }
}
