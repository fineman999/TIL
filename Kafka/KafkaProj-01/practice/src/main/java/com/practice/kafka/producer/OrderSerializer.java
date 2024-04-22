package com.practice.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.practice.kafka.model.OrderModel;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderSerializer implements Serializer<OrderModel> {
  private static final Logger logger = LoggerFactory.getLogger(OrderSerializer.class.getName());
  private static final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper().registerModule(new JavaTimeModule());

  @Override
  public byte[] serialize(final String s, final OrderModel orderModel) {
    try {
      return OBJECT_MAPPER.writeValueAsBytes(orderModel);
    } catch (JsonProcessingException e) {
      logger.error("Error in serializing order model: {}", e.getMessage());
    }
    return new byte[0];
  }
}
