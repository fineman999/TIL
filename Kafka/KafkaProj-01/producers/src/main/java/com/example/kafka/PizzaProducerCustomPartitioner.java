package com.example.kafka;

import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PizzaProducerCustomPartitioner {
  private static final Logger logger =
      LoggerFactory.getLogger(PizzaProducerCustomPartitioner.class.getName());

  public static void sendPizzaMessage(
      final KafkaProducer<String, String> producer,
      final String topicName,
      final int iterCount,
      final int interIntervalMillis,
      final int intervalMillis,
      final int intervalCount,
      final boolean sync) {
    int iterSeq = 0;
    final PizzaMessage pizzaMessage = new PizzaMessage();
    long seed = 2022;
    Random random = new Random(seed);
    Faker faker = Faker.instance(random);
    while (iterSeq != iterCount) {
      HashMap<String, String> pMessageMap = pizzaMessage.produce_msg(faker, random, iterSeq);
      String key = pMessageMap.get("key");
      String message = pMessageMap.get("message");
      ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, key, message);
      sendMessage(producer, producerRecord, pMessageMap, sync);

      if (intervalCount > 0 && (iterSeq + 1) % intervalCount == 0) {
        try {
          logger.info("##### IntervalCount: {}, intervalMillis: {}", intervalCount, intervalMillis);
          Thread.sleep(intervalMillis);
        } catch (InterruptedException e) {
          logger.error(e.getMessage());
        }
      }

      if (interIntervalMillis > 0) {
        try {
          logger.info("##### interIntervalMillis: {}", interIntervalMillis);
          Thread.sleep(interIntervalMillis);
        } catch (InterruptedException e) {
          logger.error(e.getMessage());
        }
      }
    }
  }

  public static void sendMessage(
      KafkaProducer<String, String> kafkaProducer,
      ProducerRecord<String, String> producerRecord,
      HashMap<String, String> pMessageMap,
      boolean sync) {
    if (!sync) {

      kafkaProducer.send(
          producerRecord,
          (recordMetadata, e) -> {
            if (e != null) {
              logger.error("Error while producing message to topic: " + recordMetadata.topic());
              e.printStackTrace();
            } else {
              logger.info(
                  "async message: "
                      + pMessageMap.get("key")
                      + " "
                      + "partition: "
                      + recordMetadata.partition()
                      + " offset:  "
                      + recordMetadata.offset());
            }
          });
    } else {
      try {
        RecordMetadata recordMetadata = kafkaProducer.send(producerRecord).get();
        logger.info(
            "sync message: "
                + pMessageMap.get("key")
                + " "
                + "partition: "
                + recordMetadata.partition()
                + " offset:  "
                + recordMetadata.offset());
      } catch (Exception e) {
        logger.error("Error while producing message to topic: " + producerRecord.topic());
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {

    String topicName = "pizza-topic";

    Properties props = new Properties();
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.setProperty(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.setProperty(
        ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.example.kafka.CustomPartitioner");
    props.setProperty("custom.special.key", "P001");

    KafkaProducer<String, String> producer = new KafkaProducer<>(props);
    sendPizzaMessage(producer, topicName, -1, 100, 0, 0, true);
  }
}
