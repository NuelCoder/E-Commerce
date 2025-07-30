package com.example.pcuhub.pcuhub.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix= "app.seed")
public class CategorySeedConfig {
    private List<String> categories;
}
