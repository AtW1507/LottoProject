package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.OneRandomNumberResponseDto;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;
@AllArgsConstructor
class RandomGenerator implements RandomNumberGenerable {

    private final int LOWER_BAND = 1;
    private final int UPPER_BAND = 99;

    private final  OneRandomNumberFetcher client;

    public Set<Integer> generateSixRandomNumbers() {
        Set<Integer> winningNumber = new HashSet<>();
        while (isSixNumbers(winningNumber)){
            OneRandomNumberResponseDto randomNumberResponseDto = client.retrieveOneRandomNumber(LOWER_BAND,UPPER_BAND);
            int randomNumber = randomNumberResponseDto.number();
            winningNumber.add(randomNumber);
        }
        return winningNumber;
    }

    private boolean isSixNumbers(Set<Integer> winningNumber){
        return winningNumber.size() < 6;
    }




}
