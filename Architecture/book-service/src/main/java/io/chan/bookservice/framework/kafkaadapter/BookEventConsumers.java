package io.chan.bookservice.framework.kafkaadapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.bookservice.application.usecase.MakeAvailableUseCase;
import io.chan.bookservice.application.usecase.MakeUnAvailableUseCase;
import io.chan.bookservice.domain.event.ItemRented;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookEventConsumers {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MakeAvailableUseCase makeAvailableUseCase;
    private final MakeUnAvailableUseCase makeUnAvailableUseCase;

    @KafkaListener(topics = "${custom.kafka.consumers.topic1.name}", groupId = "${custom.kafka.consumers.group-id}")
    public void consumeRental(ConsumerRecord<String, String> record) {

        try {
            final ItemRented itemRented = objectMapper.readValue(record.value(), ItemRented.class);
            makeUnAvailableUseCase.unavailable(itemRented.getItem().getNo());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${custom.kafka.consumers.topic2.name}", groupId = "${custom.kafka.consumers.group-id}")
    public void consumeReturn(ConsumerRecord<String, String> record) {
        try {
            final ItemRented itemRented = objectMapper.readValue(record.value(), ItemRented.class);
            makeAvailableUseCase.available(itemRented.getItem().getNo());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
