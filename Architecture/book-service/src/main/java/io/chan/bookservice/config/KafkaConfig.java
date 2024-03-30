package io.chan.bookservice.config;

import io.chan.bookservice.domain.event.EventResult;
import io.chan.bookservice.domain.event.ItemRented;
import io.chan.bookservice.domain.event.ItemReturned;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${custom.kafka.producers.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory(String.class));
    }

    @Bean
    public KafkaTemplate<String, ItemRented> itemRentedKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory(ItemRented.class));
    }

    @Bean
    public KafkaTemplate<String, EventResult> eventResultKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory(EventResult.class));
    }

    @Bean
    public KafkaTemplate<String, ItemReturned> itemReturnedKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory(ItemReturned.class));
    }

    private <T> ProducerFactory<String, T> producerFactory(Class<T> type) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }
}