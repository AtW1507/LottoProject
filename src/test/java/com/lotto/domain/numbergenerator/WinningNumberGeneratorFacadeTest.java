package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WinningNumberGeneratorFacadeTest {

    private final WinningNumbersRepository winningNumbersRepository = new WinningNumberRepositoryTestImpl();
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);


    @Test
    public void should_return_set_of_required_size(){
        //given
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumberGeneratorFacade numberGenerator = new NumberGeneratorConfiguration().createForTest(generator,winningNumbersRepository,numberReceiverFacade);
        //when
        WinningNumbersDto generateNumbers = numberGenerator.generateWinningNumbers();
        //then
        assertThat(generateNumbers.getWinningNumbers().size()).isEqualTo(6);
    }
    @Test
    public void should_return_set_of_require_size_within_required_range(){
        //given
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumberGeneratorFacade numberGenerator = new NumberGeneratorConfiguration().createForTest(generator,winningNumbersRepository,numberReceiverFacade);
        //when
        WinningNumbersDto generatedNumbers = numberGenerator.generateWinningNumbers();
        //then
        int upperBand = 99;
        int lowerBand = 1;
        Set<Integer> winningNumbers = generatedNumbers.getWinningNumbers();
        boolean numbersInRange = winningNumbers.stream().allMatch(number -> number >= lowerBand && number <= upperBand);
        assertThat(numbersInRange).isTrue();
    }
    @Test
    public void should_throw_an_exception_when_number_not_in_range(){
        //given
        Set<Integer> numbersOutOfRange = Set.of(1,2,3,4,5,100);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl(numbersOutOfRange);
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumberGeneratorFacade numberGenerator = new NumberGeneratorConfiguration().createForTest(generator,winningNumbersRepository,numberReceiverFacade);
        //when
        //then
        assertThrows(IllegalStateException.class, numberGenerator::generateWinningNumbers,"Numbers out of range!");
    }
    @Test
    public void should_return_collection_of_unique_values(){
        //given
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumberGeneratorFacade numberGenerator = new NumberGeneratorConfiguration().createForTest(generator,winningNumbersRepository,numberReceiverFacade);
        //when
        WinningNumbersDto result = numberGenerator.generateWinningNumbers();
        //then
        int resultSize = new HashSet<>(result.getWinningNumbers()).size();
        assertThat(resultSize).isEqualTo(6);
    }
    @Test
    public void should_return_winning_numbers_by_given_date(){
        LocalDateTime drawDate = LocalDateTime.of(2026, 5, 9, 12, 0, 0);
        Set<Integer> generatedNumbers = Set.of(1, 2, 3, 4, 5, 6);
        String id = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .id(id)
                .date(drawDate)
                .winningNumbers(generatedNumbers)
                .build();
        winningNumbersRepository.save(winningNumbers);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumberGeneratorFacade numberGenerator = new NumberGeneratorConfiguration().createForTest(generator,winningNumbersRepository,numberReceiverFacade);
        //when
        WinningNumbersDto winningNumbersResult = numberGenerator.retrieveWinningNumberByDate(drawDate);
        //then
        WinningNumbersDto expectedWinningNumbersDto = WinningNumbersDto.builder()
                .date(drawDate)
                .winningNumbers(generatedNumbers)
                .build();
        assertThat(expectedWinningNumbersDto).isEqualTo(winningNumbersResult);
    }
    @Test
    public void should_throw_an_exception_when_fail_to_retrieve_numbers_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2026, 5, 9, 12, 0, 0);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumberGeneratorFacade numberGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        Throwable throwable = catchThrowable(() -> numberGenerator.retrieveWinningNumberByDate(drawDate));
        //then
        assertThat(throwable).isInstanceOf(WinningNumbersNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Not Found");
    }
    @Test
    public void should_return_true_if_numbers_are_generated_by_given_date(){
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        Set<Integer> generatedWinningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        String id = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .id(id)
                .date(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();
        winningNumbersRepository.save(winningNumbers);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumberGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        boolean areWinningNumbersGeneratedByDate = numbersGenerator.areWinningNumbersGeneratedByDate();
        //then
        assertTrue(areWinningNumbersGeneratedByDate);
    }

}