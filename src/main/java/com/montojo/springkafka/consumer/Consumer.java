package com.montojo.springkafka.consumer;

import com.montojo.springkafka.dao.Message;
import com.montojo.springkafka.service.SavingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @Autowired
    SavingService savingService;

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);


    @KafkaListener(topics ="${spring.kafka.topic.name}", concurrency = "${spring.kafka.consumer.level.concurrency:3}")
    public void pollMessages(@Payload Message message,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                             @Header(KafkaHeaders.OFFSET) Long offset){

        savingService.saveMessage(message);

        logger.info("\nReceived a message contains message: {}, from topic: {}, partition: {}, offset {}",
                message.getMessage(), topic, partition, offset);
    }


}
