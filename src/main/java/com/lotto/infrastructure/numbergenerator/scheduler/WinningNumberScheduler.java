package com.lotto.infrastructure.numbergenerator.scheduler;

import com.lotto.domain.numbergenerator.WinningNumberGeneratorFacade;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class WinningNumberScheduler {

    private final WinningNumberGeneratorFacade winningNumberGeneratorFacade;



    @Scheduled(cron = "${lotto.number-generator.lotteryRunOccurrence}")
    public void generatedWinningNumbers(){
        log.info("winning number scheduler started");
        WinningNumbersDto winningNumbersDto = winningNumberGeneratorFacade.generateWinningNumbers();
        log.info(winningNumbersDto);
        log.info(winningNumbersDto.getDate());
    }
}
