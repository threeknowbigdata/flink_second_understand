package com.flink.datastream.source

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
/*
Flink在流处理上常见的Source
Flink在流处理上的source和在批处理上的source基本一致。大致有4大类
基于本地集合的source（Collection-based-source）
基于文件的source（File-based-source）- 读取文本文件，即符合 TextInputFormat 规范的文件，并将其作为字符串返回
基于网络套接字的source（Socket-based-source）- 从 socket 读取。元素可以用分隔符切分。
自定义的source（Custom-source）

 */


object StreamDataSourceDemo {

  def main(args: Array[String]): Unit = {

    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //一 基于本地集合的source（Collection-based-source）

    //0.用element创建DataStream(fromElements)
    val ds0: DataStream[String] = senv.fromElements("flink","spark","storm","mapreduce")


     ds0.print()

    //1.用Tuple创建DataStream(fromElements)
    val ds1: DataStream[(Int, String)] = senv.fromElements((1,"Spark"),(2,"Flink"),(3,"Storm"))

     ds1.print()

    //2.用Array创建DataStream
    val ds2: DataStream[String] = senv.fromCollection(Array("flink","spark"))

    ds2.print()

    //3.用ArrayBuffer创建DataStream
    senv.fromCollection(ArrayBuffer("flink","spark")).print()

    //4.用List创建DataStream
    val ds4: DataStream[String] = senv.fromCollection(List("spark", "flink"))
    ds4.print()

    //5.用List创建DataStream
    val ds5: DataStream[String] = senv.fromCollection(ListBuffer("spark", "flink"))
    ds5.print()

    senv.execute()

  }

}

