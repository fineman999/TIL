package io.chan.bookservice.framework.kafkaadapter;

import io.chan.bookservice.domain.event.EventResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookEventProducers {
    @Value("${custom.kafka.producers.topic1.name}")
    private String topic1;
    private final KafkaTemplate<String, EventResult> kafkaTemplate;

    public void occurEvent(EventResult eventResult) {
        final CompletableFuture<SendResult<String, EventResult>> future = kafkaTemplate.send(topic1, eventResult);
        future.thenApply(sendResult -> {
            log.info("Sent message=[{}] with offset=[{}]", eventResult, sendResult.getRecordMetadata().offset());
            return sendResult;
        }).exceptionally(e -> {
            log.error("Unable to send message=[{}] due to : {}", eventResult, e.getMessage());
            return null;
        });
    }
}
