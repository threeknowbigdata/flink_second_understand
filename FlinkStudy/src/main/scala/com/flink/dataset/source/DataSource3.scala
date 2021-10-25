package com.flink.dataset.source

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._
object DataSource3 {
  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    /*
    1. 读取本地文件数据 readTextFile
    2. 读取HDFS文件数据
    3. 读取CSV文件数据
    4. 读取压缩文件
    5.遍历目录
 */
    // 1. 读取本地文件数据 readTextFile
    val wordDs: DataSet[String] = env.readTextFile("data/sourceData.txt")

    //2. 读取HDFS文件数据
   // val hdfsDs: DataSet[String] = env.readTextFile("hdfs://192.168.244.169:9000/aaa.txt")

    // 读取cvs文件需要准备一个case class类


    //  3. 读取CSV文件数据
    val ds3: DataSet[Subject] = env.readCsvFile[Subject]("data/subject.csv")

    //   5 遍历读取文件夹数据
    val config = new Configuration()
    config.setBoolean("recursive.file.enumeration",true)

   // val ds5: DataSet[String] = env.readTextFile("G:/data/word/")


     wordDs.print()

    print("................................")

    //  hdfsDs.print()

    print("................................")

      ds3.print()

    print("................................")

   // ds5.print()

  }
}

case class Subject(id:Int,name:String)
