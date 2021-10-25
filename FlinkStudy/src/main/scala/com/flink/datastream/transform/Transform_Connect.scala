package com.flink.datastream.transform

import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.apache.flink.streaming.api.scala.{ConnectedStreams, DataStream, StreamExecutionEnvironment}

import java.util.concurrent.TimeUnit


object Transform_Connect {
  def main(args: Array[String]): Unit = {
    //1.准备环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //2.获取数据
    import org.apache.flink.api.scala._
    val text1: DataStream[Long] = env.addSource(new MyLongSourceScala)
    val text2: DataStream[String] = env.addSource(new MyStringSourceScala)
    //3.处理数据,使用connect
    val connectData: ConnectedStreams[Long, String] = text1.connect(text2)
    val result: DataStream[String] = connectData.map[String](x1 => "Long: " + x1, x2 => "String :" + x2)
    //4输出结果
    result.print().setParallelism(1)
    //5.启动执行
    env.execute()
  }

  /**
   * 自定义source实现从1开始产生递增数字
   */
  class MyLongSourceScala extends SourceFunction[Long] {
    var count = 1L
    var isRunning = true

    override def run(ctx: SourceContext[Long]) = {
      while (isRunning) {
        ctx.collect(count)
        count += 1
        TimeUnit.SECONDS.sleep(1)
      }
    }

    override def cancel() = {
      isRunning = false
    }
  }

  /**
   * 自定义source实现从1开始产生递增字符串
   */
  class MyStringSourceScala extends SourceFunction[String] {
    var count = 1L
    var isRunning = true

    override def run(ctx: SourceContext[String]) = {
      while (isRunning) {
        ctx.collect("str_" + count)
        count += 1
        TimeUnit.SECONDS.sleep(1)
      }
    }

    override def cancel() = {
      isRunning = false
    }
  }
}
