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
    props.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "6000");

    pollAutoCommit(props, topicName);
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
        try {
          logger.info("main thread is sleeping {} ms during while loop", 10000);
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    } catch (WakeupException e) {
      logger.error("Wake up the consumer");
    }
  }

}
