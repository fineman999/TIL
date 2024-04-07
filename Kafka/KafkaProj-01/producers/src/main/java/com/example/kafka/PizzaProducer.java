package com.example.kafka;

import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import com.github.javafaker.Faker;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PizzaProducer {
  private static final Logger logger = LoggerFactory.getLogger(PizzaProducer.class.getName());

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
//    props.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "6");
//    props.setProperty(ProducerConfig.ACKS_CONFIG, "all");
//    props.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
//    props.setProperty(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, "50000");
//    props.setProperty(ProducerConfig.ACKS_CONFIG, "0"); // acks=0 -> 동기로 보내면 offset을 가져오지 못한다.파티션은 메타데이터 기반으로 알고있기 때문에 받음
//    props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "16384"); // 16KB
//    props.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1"); // 1ms

    KafkaProducer<String, String> producer = new KafkaProducer<>(props);
    sendPizzaMessage(producer, topicName, -1, 500, 0, 0, true);
  }
}
