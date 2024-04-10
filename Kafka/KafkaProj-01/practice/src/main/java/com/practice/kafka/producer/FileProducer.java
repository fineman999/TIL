package com.practice.kafka.producer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileProducer {
  private static final Logger logger = LoggerFactory.getLogger(FileProducer.class.getName());

  public static void main(String[] args) {

    String topicName = "file-topic";

    Properties props = new Properties();
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.setProperty(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // KafkaProducer 객체 생성 -> ProducerRecords 객체 생성 -> send() 메소드 호출
    try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
      String filePath =
          "/Users/chanpark/MyCode/TIL/Kafka/KafkaProj-01/practice/src/main/resources/pizza_sample.txt";
      sendFileMessage(producer, topicName, filePath);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  private static void sendFileMessage(
      final KafkaProducer<String, String> producer, final String topicName, final String filePath) {
    try {
      FileReader fileReader = new FileReader(filePath);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line;
      final String delimiter = ",";
      while ((line = bufferedReader.readLine()) != null) {
        String[] tokens = line.split(delimiter);
        String key = tokens[0];
        StringBuffer value = new StringBuffer();
        for (int i = 1; i < tokens.length; i++) {
          value.append(tokens[i]);
          if (i < tokens.length - 1) {
            value.append(",");
          }
        }
        sendMessage(producer, topicName, key, value.toString());
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private static void sendMessage(
      final KafkaProducer<String, String> producer,
      final String topicName,
      final String key,
      final String string) {

    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, key, string);
    producer.send(
        producerRecord,
        (recordMetadata, e) -> {
          if (e != null) {
            logger.error("Error while producing message to topic: " + recordMetadata.topic());
            e.printStackTrace();
          } else {
            logger.info(
                "message: "
                    + key
                    + " partition: "
                    + recordMetadata.partition()
                    + " offset: "
                    + recordMetadata.offset());
          }
        });
  }
}
