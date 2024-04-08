package com.example.kafka;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerMTopicRebalance {
  private static final Logger logger =
      LoggerFactory.getLogger(ConsumerMTopicRebalance.class.getName());

  public static void main(String[] args) {

    Properties props = new Properties();
    props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.setProperty(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group-mtopic");
//    props.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());
    props.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());

    try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props)) {
      kafkaConsumer.subscribe(List.of("topic-p3-t1", "topic-p3-t2"));

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
