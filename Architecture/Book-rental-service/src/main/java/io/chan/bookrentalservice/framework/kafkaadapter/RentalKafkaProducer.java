package io.chan.bookrentalservice.framework.kafkaadapter;

import io.chan.bookrentalservice.application.outputport.EventOutputPort;
import io.chan.bookrentalservice.domain.model.event.ItemRented;
import io.chan.bookrentalservice.domain.model.event.ItemReturned;
import io.chan.bookrentalservice.domain.model.event.OverdueCleared;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class RentalKafkaProducer implements EventOutputPort {
    public static final String SENT_MESSAGE_WITH_OFFSET = "Sent message=[{}] with offset=[{}]";
    public static final String UNABLE_TO_SEND_MESSAGE_DUE_TO = "Unable to send message=[{}] due to : {}";
    private final KafkaTemplate<String, ItemRented> itemRentedKafkaTemplate;
    private final KafkaTemplate<String, ItemReturned> itemReturnedKafkaTemplate;
    private final KafkaTemplate<String, OverdueCleared> overdueClearedKafkaTemplate;

    @Value("${custom.kafka.producers.topic1.name}")
    private String topicRent;

    @Value("${custom.kafka.producers.topic2.name}")
    private String topicReturn;

    @Value("${custom.kafka.producers.topic3.name}")
    private String topicOverdue;

    @Override
    public void occurRentalEvent(final ItemRented itemRented) {
        final CompletableFuture<SendResult<String, ItemRented>> future = itemRentedKafkaTemplate.send(topicRent, itemRented);
        future.thenAccept(sendResult -> log.info(SENT_MESSAGE_WITH_OFFSET, itemRented, sendResult.getRecordMetadata().offset()))
                .exceptionally(e -> {
                    log.error(UNABLE_TO_SEND_MESSAGE_DUE_TO, itemRented, e.getMessage());
                    throw new RuntimeException(e);
                });
    }

    @Override
    public void occurReturnEvent(final ItemReturned itemReturned) {
        final CompletableFuture<SendResult<String, ItemReturned>> future = itemReturnedKafkaTemplate.send(topicReturn, itemReturned);
        future.thenAccept(sendResult -> log.info(SENT_MESSAGE_WITH_OFFSET, itemReturned, sendResult.getRecordMetadata().offset()))
                .exceptionally(e -> {
                    log.error(UNABLE_TO_SEND_MESSAGE_DUE_TO, itemReturned, e.getMessage());
                    throw new RuntimeException(e);
                });
    }

    @Override
    public void occurOverdueClearedEvent(final OverdueCleared overdueCleared) {
        final CompletableFuture<SendResult<String, OverdueCleared>> future = overdueClearedKafkaTemplate.send(topicOverdue, overdueCleared);
        future.thenAccept(sendResult -> log.info(SENT_MESSAGE_WITH_OFFSET, overdueCleared, sendResult.getRecordMetadata().offset()))
                .exceptionally(e -> {
                    log.error(UNABLE_TO_SEND_MESSAGE_DUE_TO, overdueCleared, e.getMessage());
                    throw new RuntimeException(e);
                });
    }
}
