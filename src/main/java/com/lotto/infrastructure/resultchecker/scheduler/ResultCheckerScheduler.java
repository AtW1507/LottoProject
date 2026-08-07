package com.lotto.infrastructure.resultchecker.scheduler;

import com.lotto.domain.numbergenerator.WinningNumberGeneratorFacade;
import com.lotto.domain.resultchecker.ResultCheckerFacade;
import com.lotto.domain.resultchecker.dto.PlayerDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@AllArgsConstructor
public class ResultCheckerScheduler {

    private final ResultCheckerFacade resultCheckerFacade;
    private final WinningNumberGeneratorFacade winningNumberGeneratorFacade;

    @Scheduled(cron = "${lotto.result-checker.resultCheckerOccurrence}")
    public PlayerDto generatedWinners(){
        log.info("ResultCheckerScheduler started ...");
        if(!winningNumberGeneratorFacade.areWinningNumbersGeneratedByDate()){
            log.error("Winning numbers are not generated");
            throw new RuntimeException("Winning numbers are not generated");
        }
        log.info("Winning numbers has been fetched");
        return resultCheckerFacade.generatedResults();
    }
}
