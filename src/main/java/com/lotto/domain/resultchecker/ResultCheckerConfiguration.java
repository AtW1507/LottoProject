package com.lotto.domain.resultchecker;

import com.lotto.domain.numbergenerator.WinningNumberGeneratorFacade;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResultCheckerConfiguration {


    @Bean
    ResultCheckerFacade resultCheckerFacade(WinningNumberGeneratorFacade generatorFacade, NumberReceiverFacade receiverFacade, PlayerRepository playerRepository){
        WinnersRetriever winnersGenerator = new WinnersRetriever();
        return new ResultCheckerFacade(receiverFacade, generatorFacade, winnersGenerator, playerRepository);
    }
}
