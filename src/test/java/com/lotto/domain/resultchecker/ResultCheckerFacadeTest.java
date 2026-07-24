package com.lotto.domain.resultchecker;

import com.lotto.domain.numbergenerator.WinningNumberGeneratorFacade;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import com.lotto.domain.resultchecker.dto.PlayerDto;
import com.lotto.domain.resultchecker.dto.ResultDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultCheckerFacadeTest {

    private final PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
    private final WinningNumberGeneratorFacade winningNumberGeneratorFacade = mock(WinningNumberGeneratorFacade.class);
    private final NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);

    @Test
    public void should_generated_all_players_with_correct_message() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2026, 5, 15, 12, 0, 0);
        when(winningNumberGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build());
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(
                List.of(TicketDto.builder()
                                .hash("001")
                                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("002")
                                .numbers(Set.of(1, 2, 3, 55, 66, 18))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("003")
                                .numbers(Set.of(1, 2, 3, 4, 8, 9))
                                .drawDate(drawDate)
                                .build())
        );
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().resultCheckerFacade(winningNumberGeneratorFacade, numberReceiverFacade, playerRepository);
        //when
        PlayerDto playerDto = resultCheckerFacade.generatedWinners();
        //then
        List<ResultDto> results = playerDto.results();
        ResultDto resultDto = ResultDto.builder()
                .hash("001")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        ResultDto resultDto2 = ResultDto.builder()
                .hash("001")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        ResultDto resultDto3 = ResultDto.builder()
                .hash("001")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        assertThat(results).contains(resultDto, resultDto2, resultDto3);
        String message = playerDto.message();
        assertThat(message).isEqualTo("Winners succeeded to retrieve");
    }

    @Test
    public void should_generate_fail_message_when_winningNumbers_equal_null() {
        //given
        when(winningNumberGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(null)
                .build());
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().resultCheckerFacade(winningNumberGeneratorFacade, numberReceiverFacade, playerRepository);
        //when
        PlayerDto playerDto = resultCheckerFacade.generatedWinners();
        //then
        String message = playerDto.message();
        assertThat(message).isEqualTo("Winners failed to retrieve");
    }

    @Test
    public void should_generate_fail_message_when_winningNumbers_is_empty() {
        //given
        when(winningNumberGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of())
                .build());
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().resultCheckerFacade(winningNumberGeneratorFacade, numberReceiverFacade, playerRepository);
        //when
        PlayerDto playerDto = resultCheckerFacade.generatedWinners();
        //then
        String message = playerDto.message();
        assertThat(message).isEqualTo("Winners failed to retrieve");
    }

    @Test
    public void should_generate_result_with_correct_credentials() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2026, 5, 12, 12, 0, 0);
        when(winningNumberGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build());
        String hash = "001";
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(List.of(TicketDto.builder()
                        .hash(hash)
                        .numbers(Set.of(7, 8, 9, 10, 11, 12))
                        .drawDate(drawDate)
                        .build(),
                TicketDto.builder()
                        .hash("002")
                        .numbers(Set.of(7, 8, 9, 10, 11, 13))
                        .drawDate(drawDate)
                        .build(),
                TicketDto.builder()
                        .hash("003")
                        .numbers(Set.of(7, 8, 9, 10, 11, 13))
                        .drawDate(drawDate)
                        .build())
        );
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().resultCheckerFacade(winningNumberGeneratorFacade, numberReceiverFacade, playerRepository);
        resultCheckerFacade.generatedWinners();
        //when
        ResultDto resultDto = resultCheckerFacade.findByHash(hash);
        //then
        ResultDto expectedResult = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(7, 8, 9, 10, 11, 12))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        assertThat(resultDto).isEqualTo(expectedResult);
    }
}