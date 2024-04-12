package com.practice.kafka.event;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class FileEventHandler implements EventHandler {
  private static final Logger logger = LoggerFactory.getLogger(FileEventHandler.class.getName());
  private KafkaProducer<String, String> producer;
  private String topicName;
  private boolean sync;

  public FileEventHandler(
      final KafkaProducer<String, String> producer, final String topicName, final boolean sync) {
    this.producer = producer;
    this.topicName = topicName;
    this.sync = sync;
  }

  @Override
  public void onMessage(final MessageEvent messageEvent)
      throws InterruptedException, ExecutionException {
    ProducerRecord<String, String> producerRecord =
        new ProducerRecord<>(this.topicName, messageEvent.getKey(), messageEvent.getMessage());
    if (sync) {
      sendMessageSync(producer, producerRecord);
    } else {
      sendMessageAsync(producer, producerRecord);
    }
  }

  private void sendMessageSync(
      final KafkaProducer<String, String> producer,
      final ProducerRecord<String, String> producerRecord) throws ExecutionException, InterruptedException {
    final RecordMetadata recordMetadata = producer.send(producerRecord).get();
    logger.info(
        "\n ###### Record metadata received ###### \n"
            + "partition: {}"
            + "\noffset: {} "
            + "\ntimestamp: {}",
        recordMetadata.partition(),
        recordMetadata.offset(),
        recordMetadata.timestamp());
  }

  private void sendMessageAsync(
      final KafkaProducer<String, String> producer,
      final ProducerRecord<String, String> producerRecord) {
    producer.send(
        producerRecord,
        (recordMetadata, e) -> {
          if (e != null) {
            logger.error("Error while producing message to topic: " + recordMetadata.topic());
            e.printStackTrace();
          } else {
            logger.info(
                "\n ###### Record metadata received ###### \n"
                    + "partition: {}"
                    + "\noffset: {} "
                    + "\ntimestamp: {}",
                recordMetadata.partition(),
                recordMetadata.offset(),
                recordMetadata.timestamp());
          }
        });
  }

  public static void main(String[] args){
    String topicName = "file-topic";
    Properties props = new Properties();
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
      FileEventHandler fileEventHandler = new FileEventHandler(producer, topicName, true);
      MessageEvent messageEvent = new MessageEvent("key00001", "this is a message");
      fileEventHandler.onMessage(messageEvent);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

  }
}
