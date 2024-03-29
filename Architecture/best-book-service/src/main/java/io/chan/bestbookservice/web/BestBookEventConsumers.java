package io.chan.bestbookservice.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.bestbookservice.domain.BestBookService;
import io.chan.bestbookservice.domain.event.ItemRented;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BestBookEventConsumers {
    private final BestBookService bestBookService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${custom.kafka.consumers.topic1.name}", groupId = "${custom.kafka.consumers.group-id}")
    public void consumeItemRentedEvent(ConsumerRecord<String, String> record) {
        try {
            final ItemRented itemRented = objectMapper.readValue(record.value(), ItemRented.class);
            bestBookService.dealBestBook(itemRented.getItem());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


}
