package io.chan.userservice.framwork.kafkaadapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.userservice.application.usecase.SavePointUseCase;
import io.chan.userservice.application.usecase.UsePointUseCase;
import io.chan.userservice.domain.event.ItemRented;
import io.chan.userservice.domain.event.ItemReturned;
import io.chan.userservice.domain.event.OverdueCleared;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberEventConsumers {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SavePointUseCase savePointUseCase;
    private final UsePointUseCase usePointUseCase;

    @KafkaListener(topics = "${custom.kafka.consumers.topic1.name}", groupId = "${custom.kafka.consumers.group-id}")
    public void consumeRent(ConsumerRecord<String, String> record) {
        try {
            final ItemRented itemRented = objectMapper.readValue(record.value(), ItemRented.class);
            savePointUseCase.savePoint(itemRented.getIdName(), itemRented.getPoint());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${custom.kafka.consumers.topic2.name}", groupId = "${custom.kafka.consumers.group-id}")
    public void consumeReturn(ConsumerRecord<String, String> record) {
        try {
            final ItemReturned itemReturned = objectMapper.readValue(record.value(), ItemReturned.class);
            usePointUseCase.userPoint(itemReturned.getIdName(), itemReturned.getPoint());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${custom.kafka.consumers.topic3.name}", groupId = "${custom.kafka.consumers.group-id}")
    public void consumeOverdue(ConsumerRecord<String, String> record) {
        try {
            final OverdueCleared overdueCleared = objectMapper.readValue(record.value(), OverdueCleared.class);
            usePointUseCase.userPoint(overdueCleared.getIdName(), overdueCleared.getPoint());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
