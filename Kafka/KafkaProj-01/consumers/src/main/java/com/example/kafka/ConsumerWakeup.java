package com.example.kafka;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerWakeup {
  private static final Logger logger = LoggerFactory.getLogger(ConsumerWakeup.class.getName());

  public static void main(String[] args) {

    String topicName = "pizza-topic";
    Properties props = new Properties();
    props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group_01");
    props.setProperty(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.setProperty(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    //    props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props)) {
      kafkaConsumer.subscribe(List.of(topicName));

      final Thread mainThread = Thread.currentThread();
      Runtime.getRuntime()
          .addShutdownHook(
              new Thread() {
                @Override
                public void run() {
                  logger.info("main thread starts to exit by calling wakeup");
                  kafkaConsumer.wakeup();
                  try {
                    mainThread.join();
                  } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                  }
                }
              });

      while (true) {
        final ConsumerRecords<String, String> consumerRecords =
            kafkaConsumer.poll(Duration.ofMillis(1000));
        consumerRecords.forEach(
            record -> {
              logger.info(
                  "key: "
                      + record.key()
                      + " partition: "
                      + record.partition()
                      + " offset: "
                      + record.offset()
                      + " value: "
                      + record.value());
            });
      }
    } catch (WakeupException e) {
      logger.error("Wake up the consumer");
    }
  }
}
