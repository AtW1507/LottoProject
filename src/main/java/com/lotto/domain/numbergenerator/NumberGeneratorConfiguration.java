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

//    @Bean
//    NumberReceiverFacade numberReceiverFacade(){
//        return new NumberReceiverFacade(null,null,null,null);
//    }

    @Bean
    WinningNumberGeneratorFacade winningNumbersGeneratorFacade(WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade, RandomNumberGenerable randomNumberGenerator, WinningNumbersGeneratorFacadeConfigurationProperties properties) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumberGeneratorFacade(numberReceiverFacade, winningNumberValidator, randomNumberGenerator, winningNumbersRepository, properties);
    }

    WinningNumberGeneratorFacade createForTest(RandomNumberGenerable generator, WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade) {
        WinningNumbersGeneratorFacadeConfigurationProperties properties = WinningNumbersGeneratorFacadeConfigurationProperties.builder()
                .upperBand(99)
                .lowerBand(1)
                .count(6)
                .build();
        return winningNumbersGeneratorFacade(winningNumbersRepository, numberReceiverFacade, generator, properties);
    }
}
