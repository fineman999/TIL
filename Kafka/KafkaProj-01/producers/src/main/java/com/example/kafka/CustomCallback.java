package com.example.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomCallback implements Callback {

  private static final Logger logger = LoggerFactory.getLogger(CustomCallback.class.getName());
  private int seq;

  public CustomCallback(int seq) {
    this.seq = seq;
  }

  @Override
  public void onCompletion(final RecordMetadata recordMetadata, final Exception e) {
    if (e != null) {
      logger.error("Error while producing message to topic: " + recordMetadata.topic());
    } else {
      logger.info(
          "seq:{} partition:{} offset:{} timestamp:{}",
          seq,
          recordMetadata.partition(),
          recordMetadata.offset(),
          recordMetadata.timestamp());
    }
  }
}
