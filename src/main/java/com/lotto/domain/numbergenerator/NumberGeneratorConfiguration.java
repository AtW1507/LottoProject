package com.lotto.domain.numbergenerator;

import com.lotto.domain.numberreceiver.NumberReceiverFacade;

class NumberGeneratorConfiguration {

    WinningNumberGeneratorFacade createdForTest(RandomNumberGenerable generator, WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade){
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumberGeneratorFacade(numberReceiverFacade, winningNumberValidator, generator, winningNumbersRepository);
    }
}
