package com.threeknowbigdata.flink.hotitems_analysis;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

/**
 * @ClassName: KafkaProducerUtil
 * @Description:
 * @Author: 土哥 on 2020/11/14 16:25
 * @Version: 1.0
 */
public class KafkaProducerUtil {
    public static void main(String[] args) throws Exception{
        writeToKafka("hotitems");
    }

    // 包装一个写入kafka的方法R
    public static void writeToKafka(String topic) throws Exception{
        // kafka 配置
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.244.129:9092");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 定义一个Kafka Producer
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        // 用缓冲方式读取文本
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/home/threeknowbigdata/workspace/javaspace/FlinkStudy/src/main/java/com/threeknowbigdata/flink/hotitems_analysis/data/UserBehavior.csv"));
        String line;
        while( (line = bufferedReader.readLine()) != null ){
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, line);
            // 用producer发送数据
            kafkaProducer.send(producerRecord);
        }
        kafkaProducer.close();
    }
}
