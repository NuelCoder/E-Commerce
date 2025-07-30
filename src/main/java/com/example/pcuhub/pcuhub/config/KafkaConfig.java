package com.example.pcuhub.pcuhub.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.pcuhub.pcuhub.dtos.ProductKafkaMessage;
import com.example.pcuhub.pcuhub.dtos.UserKafkaMessage;


@EnableKafka
@Configuration
public class KafkaConfig {
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserKafkaMessage> userKafkaListenerFactory(ConsumerFactory<String, UserKafkaMessage> userConsumerFactory) {
            ConcurrentKafkaListenerContainerFactory<String, UserKafkaMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(userConsumerFactory);
            return factory;
    }
  
    public ConsumerFactory<String, UserKafkaMessage> userConsumerFactory(){
        JsonDeserializer<UserKafkaMessage> deserializer = new JsonDeserializer<>(UserKafkaMessage.class);
        deserializer.addTrustedPackages("*");
        
        ErrorHandlingDeserializer<UserKafkaMessage> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(deserializer);
        
        
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); 
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "user-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.pcuhub.pcuhub.dtos.UserKafkaMessage");
        
        
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), errorHandlingDeserializer);
    }
    
@Bean
     public ConcurrentKafkaListenerContainerFactory<String, UserKafkaMessage> pendingUserKafkaListenerFactory(ConsumerFactory<String,UserKafkaMessage> pendingUserConsumerFactory){
       ConcurrentKafkaListenerContainerFactory<String, UserKafkaMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(pendingUserConsumerFactory);
        return factory;
     }

  
     public ConsumerFactory<String, UserKafkaMessage> pendingUserConsumerFactory(){
        JsonDeserializer<UserKafkaMessage> deserializer = new JsonDeserializer<>(UserKafkaMessage.class);
        deserializer.addTrustedPackages("*");

        ErrorHandlingDeserializer<UserKafkaMessage> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(deserializer);

        Map<String,Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "pending-user-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.pcuhub.pcuhub.dtos.UserKafkaMessage");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), errorHandlingDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductKafkaMessage> productKafkaListenerFactory(ConsumerFactory<String,ProductKafkaMessage> productConsumerFactory){
        ConcurrentKafkaListenerContainerFactory<String, ProductKafkaMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(productConsumerFactory);
        return factory;
    }

    public ConsumerFactory<String, ProductKafkaMessage> productConsumerFactory(){
        JsonDeserializer<ProductKafkaMessage> deserializer = new JsonDeserializer<>(ProductKafkaMessage.class);
        deserializer.addTrustedPackages("*");

        ErrorHandlingDeserializer<ProductKafkaMessage> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(deserializer);

        Map<String,Object> props = new HashMap<>();
        
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "product-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.pcuhub.pcuhub.dtos.ProductKafkaMessage");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), errorHandlingDeserializer);
    }
}
