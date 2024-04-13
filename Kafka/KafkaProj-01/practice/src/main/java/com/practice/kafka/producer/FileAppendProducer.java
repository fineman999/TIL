package com.practice.kafka.producer;

import com.practice.kafka.event.EventHandler;
import com.practice.kafka.event.FileEventHandler;
import com.practice.kafka.event.FileEventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class FileAppendProducer {
  private static final Logger logger = LoggerFactory.getLogger(FileAppendProducer.class.getName());

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
          "/Users/chanpark/MyCode/TIL/Kafka/KafkaProj-01/practice/src/main/resources/pizza_append.txt";
      File file = new File(filePath);
      EventHandler eventHandler = new FileEventHandler(producer, topicName, false);
      FileEventSource fileEventSource = new FileEventSource(file, 100, eventHandler);
      Thread thread = new Thread(fileEventSource);
      thread.start();

      try {
        thread.join();
      } catch (InterruptedException e) {
        logger.error(e.getMessage());
      }

    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }
}
