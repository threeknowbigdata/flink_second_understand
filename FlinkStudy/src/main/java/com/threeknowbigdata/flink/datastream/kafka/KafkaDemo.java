package com.threeknowbigdata.flink.datastream.kafka;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

/**
 * 类描述：
 *
 * @ClassName KafkaDemo
 * @Description:
 * @Author: 土哥
 * @Date: 2021/9/1 下午4:41
 */
public class KafkaDemo {
    public static void main(String[] args) throws Exception {

        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //默认情况下，检查点被禁用。要启用检查点，请在StreamExecutionEnvironment上调用enableCheckpointing(n)方法，
        // 其中n是以毫秒为单位的检查点间隔。每隔500 ms进行启动一个检查点,则下一个检查点将在上一个检查点完成后5秒钟内启动

        env.enableCheckpointing(500);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.244.129:9092");//kafka的节点的IP或者hostName，多个使用逗号分隔
        properties.setProperty("zookeeper.connect", "192.168.244.129:2181");//zookeeper的节点的IP或者hostName，多个使用逗号进行分隔
        properties.setProperty("group.id", "test-consumer-group");//flink consumer flink的消费者的group.id
        System.out.println("11111111111");
        FlinkKafkaConsumer<String> myConsumer = new FlinkKafkaConsumer<String>("kafka_data_test", new SimpleStringSchema(), properties);
        // FlinkKafkaConsumer010<String> myConsumer = new FlinkKafkaConsumer010<String>("test",new SimpleStringSchema(),properties);//test0是kafka中开启的topic
        myConsumer.assignTimestampsAndWatermarks(new MessageWaterEmitter());
        DataStream<String> keyedStream = env.addSource(myConsumer);//将kafka生产者发来的数据进行处理，本例子我进任何处理
        System.out.println("2222222222222");
        keyedStream.print();//直接将从生产者接收到的数据在控制台上进行打印
        // execute program
        System.out.println("3333333333333");
        env.execute("Flink Streaming Java API Skeleton");

    }
}
