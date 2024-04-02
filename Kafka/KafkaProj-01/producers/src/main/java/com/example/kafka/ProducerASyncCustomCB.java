package com.example.kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerASyncCustomCB {

    public static void main(String[] args) {

    String topicName = "multipart-topic";

    Properties props = new Properties();
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
    props.setProperty(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // KafkaProducer 객체 생성
    KafkaProducer<Integer, String> producer = new KafkaProducer<>(props);

    for (int seq = 0; seq < 20; seq++) {
      // ProducerRecord 객체 생성 - 메시지에다가 토픽 이름과 메시지를 넣어준다.
      ProducerRecord<Integer, String> producerRecord =
          new ProducerRecord<>(topicName, seq, "hello world" + seq);

      Callback callback = new CustomCallback(seq);

      // kafkaProducer 메시지 전송
      producer.send(producerRecord, callback);
    }
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    producer.close();
  }
}
