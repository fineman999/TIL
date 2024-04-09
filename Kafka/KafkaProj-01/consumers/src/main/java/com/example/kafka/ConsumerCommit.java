package com.example.kafka;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerCommit {
  private static final Logger logger = LoggerFactory.getLogger(ConsumerCommit.class.getName());

  public static void main(String[] args) {

    String topicName = "pizza-topic";
    Properties props = new Properties();
    props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group_03");
    props.setProperty(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.setProperty(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

    //    pollAutoCommit(props, topicName);
    //    pollCommitSync(props, topicName);
    pollCommitAsync(props, topicName);
  }

  private static void pollCommitAsync(final Properties props, final String topicName) {
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

      int loopCnt = 0;
      while (true) {
        final ConsumerRecords<String, String> consumerRecords =
            kafkaConsumer.poll(Duration.ofMillis(1000));
        logger.info(
            "######## loopCnt: {} consumerRecords count: {}", loopCnt++, consumerRecords.count());
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
        kafkaConsumer.commitAsync(new OffsetCommitCallback() {
          @Override
          public void onComplete(final Map<TopicPartition, OffsetAndMetadata> offsets, final Exception e) {
            if (e != null) {
              logger.error("Commit failed for offsets {}", offsets, e);
            } else {
              logger.info("Commit succeeded for offsets {}", offsets);
            }
          }
        });
      }
    } catch (WakeupException e) {
      logger.error("Wake up the consumer");
    }
  }

  private static void pollCommitSync(final Properties props, final String topicName) {
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

      int loopCnt = 0;
      while (true) {
        final ConsumerRecords<String, String> consumerRecords =
            kafkaConsumer.poll(Duration.ofMillis(1000));
        logger.info(
            "######## loopCnt: {} consumerRecords count: {}", loopCnt++, consumerRecords.count());
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
        try {
          if (consumerRecords.count() > 0) kafkaConsumer.commitSync();
          logger.info("commitSync is called");
        } catch (CommitFailedException e) {
          logger.error("Commit failed");
        }
      }
    } catch (WakeupException e) {
      logger.error("Wake up the consumer");
    }
  }

  private static void pollAutoCommit(Properties props, String topicName) {
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

      int loopCnt = 0;
      while (true) {
        final ConsumerRecords<String, String> consumerRecords =
            kafkaConsumer.poll(Duration.ofMillis(1000));
        logger.info(
            "######## loopCnt: {} consumerRecords count: {}", loopCnt++, consumerRecords.count());
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
