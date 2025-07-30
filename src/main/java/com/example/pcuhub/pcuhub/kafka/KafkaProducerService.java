package com.example.pcuhub.pcuhub.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.pcuhub.pcuhub.dtos.ProductKafkaMessage;
import com.example.pcuhub.pcuhub.dtos.UserKafkaMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, UserKafkaMessage> userKafkaTemplate;
    private final KafkaTemplate<String, ProductKafkaMessage> productKafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private static final String USER_TOPIC = "delayed-user-topic";
    private static final String PRODUCT_TOPIC = "delayed-product-topic";


    public void sendUserKafkaMessage(UserKafkaMessage message){
        logger.info("Producing UserKafkaMessage: {}", message);
        userKafkaTemplate.send(USER_TOPIC, message);
    }

    public void sendProductKafkaMessage(ProductKafkaMessage message){
        logger.info("Producing ProductKafkaMessage: {}", message);
        productKafkaTemplate.send(PRODUCT_TOPIC,message);
    }

}
