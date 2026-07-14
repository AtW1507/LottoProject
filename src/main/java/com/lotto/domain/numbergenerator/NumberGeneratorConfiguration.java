package com.lotto.domain.numbergenerator;

import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
class NumberGeneratorConfiguration {

    @Bean
    WinningNumbersRepository repository(){
       return new WinningNumbersRepository() {
            @Override
            public Optional<WinningNumbers> findNumbersByDate(final LocalDateTime date) {
                return Optional.empty();
            }

            @Override
            public boolean existsByDate(final LocalDateTime nextDrawDate) {
                return false;
            }

            @Override
            public WinningNumbers save(final WinningNumbers winningNumbers) {
                return null;
            }
        };
    }

    @Bean
    NumberReceiverFacade numberReceiverFacade(){
        return new NumberReceiverFacade(null,null,null,null);
    }

    @Bean
    WinningNumberGeneratorFacade winningNumberGeneratorFacade(WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade, RandomNumberGenerable randomNumberGenerator, WinningNumbersGeneratorFacadeConfigurationProperties properties ){
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumberGeneratorFacade(numberReceiverFacade, winningNumberValidator, randomNumberGenerator, winningNumbersRepository,  properties);

    }
    WinningNumberGeneratorFacade createdForTest(RandomNumberGenerable generator, WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade){
        WinningNumbersGeneratorFacadeConfigurationProperties properties = WinningNumbersGeneratorFacadeConfigurationProperties.builder()
                .count(6)
                .upperBand(99)
                .lowerBand(1)
                .build();
        return winningNumberGeneratorFacade(winningNumbersRepository, numberReceiverFacade,generator, properties);
    }
}
