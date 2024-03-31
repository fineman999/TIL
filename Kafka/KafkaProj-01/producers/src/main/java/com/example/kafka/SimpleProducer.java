package com.example.kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class SimpleProducer {
  public static void main(String[] args) {

    String topicName = "simple-topic";
    // KafkaProducer Configuration Setting
    // null, "hello world"
    Properties props = new Properties();
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.setProperty(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // KafkaProducer 객체 생성
    KafkaProducer<String, String> producer = new KafkaProducer<>(props);

    // ProducerRecord 객체 생성 - 메시지에다가 토픽 이름과 메시지를 넣어준다.
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, "hello world");

    // kafkaProducer 메시지 전송
    producer.send(producerRecord);

    // batch에 있는 모든 메시지를 전송
    producer.flush();

    // KafkaProducer 객체 종료
    producer.close();
  }
}
