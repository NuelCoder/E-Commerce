package com.example.pcuhub.pcuhub.kafka;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.pcuhub.pcuhub.dtos.ProductKafkaMessage;
import com.example.pcuhub.pcuhub.dtos.UserKafkaMessage;
import com.example.pcuhub.pcuhub.service.PRODUCT.ProductService;
import com.example.pcuhub.pcuhub.service.PendingUser.PendingUserService;
import com.example.pcuhub.pcuhub.service.USER.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final UserService userService;
    private final PendingUserService pendingUserService;
    private final ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private static final long DELAY_MILLIS = 1*60*1000;

    @KafkaListener(topics= "delayed-user-topic", containerFactory= "userKafkaListenerFactory")
    public void consumeUserMessage(ConsumerRecord<String, UserKafkaMessage> record){
        UserKafkaMessage message = record.value();
        logger.info("[USER] Kafka message received: {}", message);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run(){
                logger.info("[USER] Processing after delay: {}", message);
                if ("USER".equalsIgnoreCase(message.getSource())) {
                    userService.processKafkaUserAction(message);
                }
            }
        },DELAY_MILLIS);
    }


    @KafkaListener(topics= "delayed-user-topic", containerFactory= "pendingUserKafkaListenerFactory")
    public void consumePendingUserMessage(ConsumerRecord<String,UserKafkaMessage>record){
        UserKafkaMessage message = record.value();
        logger.info("[PENDING_USER] Kafka message received: {}",message);




        
        new Timer().schedule(new TimerTask() {
            @Override
            public void run(){
                logger.info("[PENDING_USER] Processing after delay: {}", message);
                if ("PENDING".equalsIgnoreCase(message.getSource())) {
                    pendingUserService.handlePendingUser(message);
                }
            }
        },DELAY_MILLIS);
    }
    @KafkaListener(topics= "delayed-product-topic", containerFactory= "productKafkaListenerFactory")
    public void consumeProductMessage(ConsumerRecord<String,ProductKafkaMessage> record){
        ProductKafkaMessage message = record.value();
        logger.info("[PRODUCT] Kafka message  received: {}", message);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run(){
                logger.info("[PRODUCT] Processing after delay: {}", message);
                productService.processKafkaProductAction(message);
            }
        },DELAY_MILLIS);
    }
   
}
