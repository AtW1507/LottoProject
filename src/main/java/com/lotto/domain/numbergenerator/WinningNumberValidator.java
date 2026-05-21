package com.lotto.domain.numbergenerator;

import java.util.Set;

class WinningNumberValidator {

    private final int LOWER_BAND = 0;
    private final int UPPER_BAND = 99;

    public Set<Integer> validate(Set<Integer> winningNumber){
        if(outOfRange(winningNumber)){
            throw new IllegalStateException("Number out of range!");
        }
        return winningNumber;
    }

    boolean outOfRange(Set<Integer> winningNumbers){
        return winningNumbers.stream()
                .anyMatch(number -> number < LOWER_BAND || number > UPPER_BAND);

    }
}
