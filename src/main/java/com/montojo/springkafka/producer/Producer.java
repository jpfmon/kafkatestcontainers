package com.montojo.springkafka.producer;

import com.montojo.springkafka.dao.Message;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private final KafkaTemplate<String, Message> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String topic;

    @Value("${spring.kafka.replication.factor}")
    private int replicationFactor;

    @Value("${spring.kafka.partition.number}")
    private int partitionNumber;

    public Producer(KafkaTemplate<String, Message> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void writeToKafka(Message message) {

        kafkaTemplate.send(topic, message);
    }

    @Bean
    @Order(-1)
    public NewTopic createNewTopic() {
        return new NewTopic(topic, partitionNumber, (short) replicationFactor);
    }
}

