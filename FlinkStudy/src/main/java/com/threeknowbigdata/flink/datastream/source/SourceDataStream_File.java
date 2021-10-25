package com.threeknowbigdata.flink.datastream.source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.List;
/*
Flink在流处理上常见的Source
Flink在流处理上的source和在批处理上的source基本一致。大致有4大类
基于本地集合的source（Collection-based-source）
基于文件的source（File-based-source）- 读取文本文件，即符合 TextInputFormat 规范的文件，并将其作为字符串返回
基于网络套接字的source（Socket-based-source）- 从 socket 读取。元素可以用分隔符切分。
自定义的source（Custom-source）

 */

/**
 * 类描述：
 *
 * @ClassName StrDataSource1
 * @Description:
 * @Author: 土哥
 * @Date: 2021/8/13 15:26
 */
public class SourceDataStream_File {


    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        //TODO 1. 基于文件集合的source（Collection-based-source）
        sEnv.setParallelism(1);

        DataStreamSource<String> stringDataStreamSource = sEnv.readTextFile("data/subject.csv");

        stringDataStreamSource.print();

        //TODO 2.读取hdfs文件
        //DataStreamSource<String> stringDataStreamSource1 = sEnv.readTextFile("hdfs://192.168.244.129:9000/aaa.txt");
        //stringDataStreamSource1.print();

       // sEnv.execute();
    }
}
