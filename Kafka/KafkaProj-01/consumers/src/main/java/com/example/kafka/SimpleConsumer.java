package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class SimpleConsumer {
  private static final Logger logger = LoggerFactory.getLogger(SimpleConsumer.class.getName());

  public static void main(String[] args) {

    String topicName = "simple-topic";
    Properties props = new Properties();
    props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "simple-group");
//    props.setProperty(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "5000");
//    props.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "90000");
//    props.setProperty(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "600000");

    props.setProperty(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.setProperty(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    final KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
    kafkaConsumer.subscribe(List.of(topicName));

    while (true) {
      final ConsumerRecords<String, String> consumerRecords =
          kafkaConsumer.poll(Duration.ofMillis(1000));
      consumerRecords.forEach(
          record -> {
            logger.info(
                "key: "
                    + record.key()
                    + " value: "
                    + record.value()
                    + " partition: "
                    + record.partition()
                    + " offset: "
                    + record.offset());
          });
    }
  }
}
