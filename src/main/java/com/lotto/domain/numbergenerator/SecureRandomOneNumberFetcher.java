package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.OneRandomNumberResponseDto;

import java.security.SecureRandom;
import java.util.Random;

class SecureRandomOneNumberFetcher implements OneRandomNumberFetcher{
    @Override
    public OneRandomNumberResponseDto retrieveOneRandomNumber(final int lowerBand, final int upperBand) {
            Random random = new SecureRandom();
            return OneRandomNumberResponseDto.builder()
                    .number(random.nextInt((upperBand - lowerBand) +1))
                    .build();
    }
}
