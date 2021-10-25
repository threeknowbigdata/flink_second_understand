package com.flink.dataset


import org.apache.flink.api.scala.{AggregateDataSet, DataSet, ExecutionEnvironment, GroupedDataSet}
import org.apache.flink.streaming.api.scala._
/**
 * @ClassName Main
 * @Description:
 * @Author: 土哥
 * @Date: 2021/8/13 11:44
 */
/*
使用flink进行单词计数
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    /*
    1.	获得一个execution environment，
    2.	加载/创建初始数据，
    3.	指定这些数据的转换，
    4.	指定将计算结果放在哪里，
    5.	触发程序执行
 */
    //1.	获得一个execution environment，
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // 设置并行度为1
    env.setParallelism(1)

    //  2.	加载/创建初始数据，
    val sourceDs: DataSet[String] = env.fromElements("Apache Flink is an open source platform for distributed stream and batch data processing",
      "Flink’s core is a streaming dataflow engine that provides data distribution")
    //思路： 对每行语句按照空格进行切分，切分之后组成单词（单词，1）tuple,按照单词进行分组，最后进行聚合计算
    // 3 指定这些数据的转换, transformation
    val wordDs: DataSet[String] = sourceDs.flatMap(_.split(" "))
    //(单词，1)
    var wordAndoneDs: DataSet[(String, Int)] = wordDs.map((_, 1))

    val groupsDs: GroupedDataSet[(String, Int)] = wordAndoneDs.groupBy(0)
    //聚合
    val aggDs: AggregateDataSet[(String, Int)] = groupsDs.sum(1)
    //4 指定将计算结果放在哪里,存到HDFS中
    aggDs.print()
    //aggDs.writeAsText("hdfs://192.168.244.129:9000/out",FileSystem.WriteMode.OVERWRITE)
    // 5 出发程序执行
    //env.execute()
  }
}
