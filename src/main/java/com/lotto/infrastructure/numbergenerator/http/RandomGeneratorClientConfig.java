package com.lotto.infrastructure.numbergenerator.http;

import com.lotto.domain.numbergenerator.RandomNumberGenerable;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RandomGeneratorClientConfig {

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(RandomNumberGeneratorRestTemplateConfigurationProperties properties,
            RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(properties.connectionTimeout()))
                .setReadTimeout(Duration.ofMillis(properties.readTimeout()))
                .errorHandler(restTemplateResponseErrorHandler)
                .build();
    }

    @Bean
    public RandomNumberGenerable remoteNumberGeneratorClient(RestTemplate restTemplate,
                                                             RandomNumberGeneratorRestTemplateConfigurationProperties properties){
        {
            return new RandomNumberGeneratorRestTemplate(restTemplate, properties.uri(), properties.port());
        }


    }
}
