package com.threeknowbigdata.flink.dataset.source;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;

/**
 * 类描述：
 *
 * @ClassName DataSource1
 * @Description: 数据读取分为四种方式
 * @Author: 土哥
 * @Date: 2021/8/13 13:49
 */
public class DataSourceExample {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //1、从内存中读取
        DataSource<String> sourceDataset = env.fromElements("ksja jadkh adkha ad");

        //2、从文件中读取
        DataSource<String> sourceDataSet1 = env.readTextFile("data/sourceData.txt");

        sourceDataset.print();
        sourceDataSet1.print();

    }
}
