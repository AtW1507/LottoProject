package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.OneRandomNumberResponseDto;

interface OneRandomNumberFetcher {
    OneRandomNumberResponseDto retrieveOneRandomNumber(int lowerBand, int upperBand);
}
