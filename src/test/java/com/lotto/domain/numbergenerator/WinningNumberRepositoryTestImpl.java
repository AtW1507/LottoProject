package com.lotto.domain.numbergenerator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class WinningNumberRepositoryTestImpl implements WinningNumbersRepository {
    Map<LocalDateTime, WinningNumbers> winningNumbersBaseTest = new ConcurrentHashMap<>();

    @Override
    public Optional<WinningNumbers> findNumbersByDate(LocalDateTime date) {
        return Optional.ofNullable(winningNumbersBaseTest.get(date));
    }

    @Override
    public boolean existsByDate(LocalDateTime nextDrawDate) {
        winningNumbersBaseTest.get(nextDrawDate);
        return true;
    }
    @Override
    public WinningNumbers save(WinningNumbers winningNumbers){
        return winningNumbersBaseTest.put(winningNumbers.drawDate(), winningNumbers);
    }

}
