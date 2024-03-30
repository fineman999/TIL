package io.chan.bookservice.framework.kafkaadapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.bookservice.application.usecase.MakeAvailableUseCase;
import io.chan.bookservice.application.usecase.MakeUnAvailableUseCase;
import io.chan.bookservice.domain.event.EventResult;
import io.chan.bookservice.domain.event.EventType;
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
    private final BookEventProducers bookEventProducers;
    @KafkaListener(topics = "${custom.kafka.consumers.topic1.name}", groupId = "${custom.kafka.consumers.group-id}")
    public void consumeRental(ConsumerRecord<String, String> record) {
        EventResult eventResult;
        try {
            final ItemRented itemRented = objectMapper.readValue(record.value(), ItemRented.class);
            eventResult = EventResult.of(itemRented, EventType.RENT, true);
            try{
                makeUnAvailableUseCase.unavailable(itemRented.getItem().getNo());
            }catch (Exception e){
                eventResult = EventResult.of(itemRented, EventType.RENT, false);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        bookEventProducers.occurEvent(eventResult);
    }

    @KafkaListener(topics = "${custom.kafka.consumers.topic2.name}", groupId = "${custom.kafka.consumers.group-id}")
    public void consumeReturn(ConsumerRecord<String, String> record) {
        EventResult eventResult;
        try {
            final ItemRented itemRented = objectMapper.readValue(record.value(), ItemRented.class);
            eventResult = EventResult.of(itemRented, EventType.RETURN, true);
            try{
                makeAvailableUseCase.available(itemRented.getItem().getNo());
            }catch (Exception e){
                eventResult = EventResult.of(itemRented, EventType.RETURN, false);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        bookEventProducers.occurEvent(eventResult);
    }
}
