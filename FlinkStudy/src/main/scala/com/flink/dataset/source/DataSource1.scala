package com.flink.dataset.source

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.streaming.api.scala._

object DataSource {
  def main(args: Array[String]): Unit = {
    /*
   dataset api 中 datasource主要有两种
    1 基于集合（Collection-based-source）
    2 基于文件(File-based-source)
     */

    //1 获取executionEnvironment
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    //2 source 操作
    // 2.1  1 基于集合
    //1.使用env.fromElements()支持Tuple，自定义对象等复合形式。
    val eleDs: DataSet[String] = env.fromElements ("mapreduce,saprk,flink")
    //2.使用env.fromCollection()支持多种Collection的具体类型
    val collDs: DataSet[String] = env.fromCollection(Array("mapreduce","saprk","flink"))
    //  3.使用env.generateSequence()支持创建基于Sequence的DataSet


    //3 转换操作

    //4 sink操作
    eleDs.print()
    collDs.print()
    //5 执行操作
  }

}

