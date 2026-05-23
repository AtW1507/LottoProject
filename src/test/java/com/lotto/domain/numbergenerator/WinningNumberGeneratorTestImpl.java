package com.lotto.domain.numbergenerator;

import java.util.Set;

class WinningNumberGeneratorTestImpl implements RandomNumberGenerable{

    private final Set<Integer> generateNumbers;


    WinningNumberGeneratorTestImpl(){
        generateNumbers =  Set.of(1,2,3,4,5,6);
    }

    WinningNumberGeneratorTestImpl(Set<Integer> generateNumbers){
        this.generateNumbers = generateNumbers;
    }


    @Override
    public Set<Integer> generateSixRandomNumbers() {
        return generateNumbers;
    }
}
