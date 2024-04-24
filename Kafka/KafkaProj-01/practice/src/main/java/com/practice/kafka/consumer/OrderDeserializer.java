package com.practice.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.practice.kafka.model.OrderModel;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderDeserializer implements Deserializer<OrderModel> {
    private static final Logger logger = LoggerFactory.getLogger(OrderDeserializer.class.getName());
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Override
    public OrderModel deserialize(final String s, final byte[] bytes) {
        OrderModel orderModel = null;
        try {
            orderModel = objectMapper.readValue(bytes, OrderModel.class);
        } catch (Exception e) {
            logger.error("Error in deserializing order model: {}", e.getMessage());
        }
        return orderModel;
    }
}
