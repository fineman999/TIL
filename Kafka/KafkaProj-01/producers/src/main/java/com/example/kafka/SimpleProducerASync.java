package com.example.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

public class SimpleProducerASync {
  private static final Logger logger = Logger.getLogger(SimpleProducerASync.class.getName());

  public static void main(String[] args) {

    String topicName = "simple-topic";

    Properties props = new Properties();
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.setProperty(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // KafkaProducer 객체 생성
    KafkaProducer<String, String> producer = new KafkaProducer<>(props);

    // ProducerRecord 객체 생성 - 메시지에다가 토픽 이름과 메시지를 넣어준다.
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, "hello world2");

    // kafkaProducer 메시지 전송
    producer.send(
        producerRecord,
        (recordMetadata, e) -> {
          if (e != null) {
              logger.warning("Error while producing message to topic: " + recordMetadata.topic());
            e.printStackTrace();
          } else {
            logger.info(
                "\n ###### Record metadata received ###### \n"
                    + "partition: "
                    + recordMetadata.partition()
                    + "\noffset:  "
                    + recordMetadata.offset()
                    + "\ntimestamp: "
                    + recordMetadata.timestamp()
                    + "\n");
          }
        });

      try {
          Thread.sleep(3000);
      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      }

      producer.close();
  }
}
