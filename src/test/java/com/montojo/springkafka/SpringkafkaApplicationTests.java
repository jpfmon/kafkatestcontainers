package com.montojo.springkafka;

import com.montojo.springkafka.consumer.Consumer;
import com.montojo.springkafka.dao.Message;
import com.montojo.springkafka.producer.Producer;
import com.montojo.springkafka.service.SavingService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Testcontainers
@SpringBootTest
class SpringkafkaApplicationTests {

    @Container
    static KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry){
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Autowired
    private Producer producer;
    @Autowired
    private Consumer consumer;
    @MockBean
    SavingService savingService;
    @Captor
    ArgumentCaptor<Message> captor;

    @Test
    void testProduceAndConsumeKafkaMessage() {

        String testMessage = "test message";
        Message messageObject = new Message(testMessage);

        producer.writeToKafka(messageObject);

        verify(savingService,timeout(1000)).saveMessage(captor.capture());
        assertNotNull(captor.getValue());
        assertEquals(testMessage,captor.getValue().getMessage());
    }

}
