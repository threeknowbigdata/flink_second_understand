package com.threeknowbigdata.flink.datastream.kafka;

import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

/**
 * 类描述：
 *
 * @ClassName MessageWaterEmitter
 * @Description:
 * @Author: 土哥
 * @Date: 2021/9/1 下午4:36
 */
public class MessageWaterEmitter implements AssignerWithPunctuatedWatermarks<String> {

    public Watermark checkAndGetNextWatermark(String lastElement, long extractedTimestamp) {
        if (lastElement != null && lastElement.contains(",")) {
            String[] parts = lastElement.split(",");
            return new Watermark(Long.parseLong(parts[0]));
        }
        return null;
    }

    public long extractTimestamp(String element, long previousElementTimestamp) {
        if (element != null && element.contains(",")) {
            String[] parts = element.split(",");
            return Long.parseLong(parts[0]);
        }
        return 0L;
    }
}
