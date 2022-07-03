package com.threeknowbigdata.flink.datastream.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class SendDataToKafka {

    public static void main(String[] args) throws Exception {
        KafkaProducer<String, String> producer;
        Properties props = new Properties();
        //指定代理服务器的地址
        props.put("bootstrap.servers", "192.168.244.131:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer(props);

        int count = 0;

        for(int i = 0; i < 200000; i++){
          // BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("G:\\pythonWorkSpace\\lightgbm_code\\Flink sql\\application_train.csv")));
          // BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/home/threeknowbigdata/workspace/javaspace/FlinkStudy/data/lightgbm_test.csv")));
          // BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("data/Home_Credit_0.7968_307507_797_test_data_notitle.csv")));
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("D:\\flink_second_understand\\FlinkStudy\\src\\main\\java\\com\\threeknowbigdata\\flink\\market_analysis\\data\\AdClickLog.csv")));
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                producer.send(new ProducerRecord<>("adClickLog", null, line));
                count++;
                if(count % 1000 == 0 ){
                    System.out.println(count);
                }

            }
            bufferedReader.close();
        }

        producer.close();
    }
}
