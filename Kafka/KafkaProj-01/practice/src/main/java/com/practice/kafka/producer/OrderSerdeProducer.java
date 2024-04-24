package com.practice.kafka.producer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import com.practice.kafka.model.OrderModel;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderSerdeProducer {
  private static final Logger logger = LoggerFactory.getLogger(OrderSerdeProducer.class.getName());

  public static void main(String[] args) {

    String topicName = "order-serde-topic";

    Properties props = new Properties();
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.setProperty(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderSerializer.class.getName());

    // KafkaProducer 객체 생성 -> ProducerRecords 객체 생성 -> send() 메소드 호출
    try (KafkaProducer<String, OrderModel> producer = new KafkaProducer<>(props)) {
      String filePath =
          "/Users/chanpark/MyCode/TIL/Kafka/KafkaProj-01/practice/src/main/resources/pizza_sample.txt";
      sendFileMessage(producer, topicName, filePath);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  private static void sendFileMessage(
      final KafkaProducer<String, OrderModel> producer,
      final String topicName,
      final String filePath) {
    try {
      FileReader fileReader = new FileReader(filePath);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line;
      final String delimiter = ",";
      while ((line = bufferedReader.readLine()) != null) {
        String[] tokens = line.split(delimiter);
        String key = tokens[0];

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        OrderModel orderModel =
            new OrderModel(
                tokens[1],
                tokens[2],
                tokens[3],
                tokens[4],
                tokens[5],
                tokens[6],
                LocalDateTime.parse(tokens[7].trim(), formatter));

        sendMessage(producer, topicName, key, orderModel);
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private static void sendMessage(
      final KafkaProducer<String, OrderModel> producer,
      final String topicName,
      final String key,
      final OrderModel orderModel) {
    ProducerRecord<String, OrderModel> producerRecord =
        new ProducerRecord<>(topicName, key, orderModel);
    logger.info("key: {}, value: {}", key, orderModel.toString());
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
