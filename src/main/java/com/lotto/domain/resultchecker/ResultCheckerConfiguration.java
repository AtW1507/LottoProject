package com.lotto.domain.resultchecker;

import com.lotto.domain.numbergenerator.WinningNumberGeneratorFacade;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;

class ResultCheckerConfiguration {
    ResultCheckerFacade createForTest(WinningNumberGeneratorFacade generatorFacade, NumberReceiverFacade receiverFacade, PlayerRepository playerRepository){
        WinnersRetriever winnersGenerator = new WinnersRetriever();
        return new ResultCheckerFacade(receiverFacade, generatorFacade, winnersGenerator, playerRepository);
    }
}
