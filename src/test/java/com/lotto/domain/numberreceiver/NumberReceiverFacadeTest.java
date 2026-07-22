package com.lotto.domain.numberreceiver;


import com.lotto.domain.AdjustableClock;
import com.lotto.domain.numberreceiver.dto.InputNumberResultDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class NumberReceiverFacadeTest {


    private final TicketRepository ticketRepository = new TicketRepositoryTestImpl();
    Clock clock = Clock.systemUTC();




    @Test
    public void should_return_success_when_user_gave_six_numbers() {
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(hashGenerator,clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();

        TicketDto generatedTicket = TicketDto.builder()
                .hash(hashGenerator.getHash())
                .numbers(numbersFromUser)
                .drawDate(nextDrawDate)
                .build();
        //when
        InputNumberResultDto result = numberReceiverFacade.inputNumber(numbersFromUser);
        //then
        InputNumberResultDto expectedResponse = new InputNumberResultDto(generatedTicket, ValidationResult.INPUT_SUCCESS.info);
        assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_message_when_user_gave_at_least_one_number_out_of_range_of_1_to_99() {
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(hashGenerator,clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 600);
        //when
        InputNumberResultDto result = numberReceiverFacade.inputNumber(numbersFromUser);
        //then
        InputNumberResultDto expectedResponse = new InputNumberResultDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_message_when_user_gave_less_than_six_numbers() {
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(hashGenerator,clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5);
        //when
        InputNumberResultDto result = numberReceiverFacade.inputNumber(numbersFromUser);
        //then
        InputNumberResultDto expectedResponse = new InputNumberResultDto(null,ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_message_when_user_gave_more_than_six_numbers() {
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(hashGenerator,clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 10);
        //when
        InputNumberResultDto result = numberReceiverFacade.inputNumber(numbersFromUser);
        //then
        InputNumberResultDto expectedResponse = new InputNumberResultDto(null,ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_save_to_database_when_user_gave_six_numbers() {
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(hashGenerator,clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        //when
        InputNumberResultDto result = numberReceiverFacade.inputNumber(numbersFromUser);
        LocalDateTime dateTime = result.ticketDto().drawDate();
        String hash = result.ticketDto().hash();
        List<TicketDto> ticketDtos = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(dateTime);
        //then
        assertThat(ticketDtos).hasSize(1);
        assertThat(ticketDtos.get(0).numbers()).containsExactlyInAnyOrder(1,2,3,4,5,6);
        assertThat(ticketDtos.get(0).hash()).isEqualTo(hash);
    }

    @Test
    public void should_return_date_next_draw(){
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        Clock clock = Clock.fixed(LocalDateTime.of(2026,5,1,12,0,0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(hashGenerator,clock, ticketRepository);
        //when
        LocalDateTime drawDate = numberReceiverFacade.retrieveNextDrawDate();
        //then
        assertThat(drawDate).isEqualTo(LocalDateTime.of(2026, 5, 2, 12, 0, 0));
    }
    @Test
    public void should_return_next_Saturday_draw_date_when_is_Saturday_noon(){
        //given
        Clock clock = Clock.fixed(LocalDateTime.of(2026, 5,6,12,0,0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        Set<Integer> numberFromUser = Set.of(1,2,3,4,5,6);

        //when
        LocalDateTime testDrawDate = numberReceiverFacade.inputNumber(numberFromUser).ticketDto().drawDate();

        //then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2026,5,9,12,0,0);
        assertThat(testDrawDate).isEqualTo(expectedDrawDate);
    }
    @Test
    public void should_return_next_Saturday_draw_date_when_is_Saturday_afternoon() {
        //given
        Clock clock = Clock.fixed(LocalDateTime.of(2026, 5, 6, 14, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        Set<Integer> numberFromUser = Set.of(1, 2, 3, 4, 5, 6);

        //when
        LocalDateTime testDrawDate = numberReceiverFacade.inputNumber(numberFromUser).ticketDto().drawDate();

        //then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2026, 5, 9, 12, 0, 0);
        assertThat(testDrawDate).isEqualTo(expectedDrawDate);
    }
    @Test
    public void should_return_empty_collection_if_there_are_no_tickets(){
        //given
        Clock clock = Clock.fixed(LocalDateTime.of(2026, 5, 6, 14, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        LocalDateTime drawDate = LocalDateTime.now(clock);

        //when
        List<TicketDto> allTickets = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);

        //then
        assertThat(allTickets).isEmpty();
    }
    @Test
    public void should_return_tickets_with_correct_draw_date(){
        //given
        HashGenerable hashGenerator = new HashGenerator();
        Instant fixedInstant = LocalDateTime.of(2026, 5, 6, 12, 0, 0).toInstant(ZoneOffset.UTC);
        ZoneId zone = ZoneId.of("Europe/London");
        AdjustableClock clock = new AdjustableClock(zone, fixedInstant);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        InputNumberResultDto numberResultDto = numberReceiverFacade.inputNumber(Set.of(1,2,3,4,5,6));
        clock.plusDays(1);
        InputNumberResultDto numberResultDto1 = numberReceiverFacade.inputNumber(Set.of(1,2,3,4,5,6));
        clock.plusDays(1);
        InputNumberResultDto numberResultDto2 = numberReceiverFacade.inputNumber(Set.of(1,2,3,4,5,6));
        clock.plusDays(1);
        InputNumberResultDto numberResultDto3 = numberReceiverFacade.inputNumber(Set.of(1,2,3,4,5,6));
        TicketDto ticketDto = numberResultDto.ticketDto();
        TicketDto ticketDto1 = numberResultDto1.ticketDto();
        TicketDto ticketDto2 = numberResultDto2.ticketDto();
        LocalDateTime drawDate = numberResultDto.ticketDto().drawDate();
        //when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);
        //then
        assertThat(allTicketsByDate).containsOnly(ticketDto, ticketDto1, ticketDto2);

    }
    @Test
    public void should_return_empty_collection_if_given_date_after_next_drawDate(){
        //given
        HashGenerable hashGenerator = new HashGenerator();
        Clock clock = Clock.fixed(LocalDateTime.of(2026,5,6,12,0,0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(hashGenerator, clock, ticketRepository);
        InputNumberResultDto numberResultDto = numberReceiverFacade.inputNumber(Set.of(1,2,3,4,5,6));
        LocalDateTime drawDate = numberResultDto.ticketDto().drawDate();

        //when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate.plusWeeks(1L));
        //then
        assertThat(allTicketsByDate).isEmpty();
    }

}