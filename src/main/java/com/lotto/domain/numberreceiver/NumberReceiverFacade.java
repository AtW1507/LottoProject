package com.lotto.domain.numberreceiver;

import com.lotto.domain.numberreceiver.dto.InputNumberResultDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/*
 *
 *
 * Klient podaje 6 liczb
 *liczby musza byc w zakresie 1-99
 * liczby nie mogą się powtarzać
 * klient dostaje informacje o dacie losowania
 * klient dostaje informacje o swoim unikalnum indetyfikatorzee losowania
 *
 * */
@AllArgsConstructor
class NumberReceiverFacade {

    private final NumberValidator validator;
    private final NumberReceiverRepository repository;
    private Clock clock;

    public InputNumberResultDto inputNumber(Set<Integer> numbersFromUser) {
        boolean areAllNumbersInRange = validator.areAllNumbersInRange(numbersFromUser);
        if (areAllNumbersInRange) {
            String tickedId = UUID.randomUUID().toString();
            LocalDateTime drawDate = LocalDateTime.now(clock);
            Ticket savedTicket = repository.save(new Ticket(tickedId, drawDate, numbersFromUser));
            return InputNumberResultDto.builder()
                    .drawDate(savedTicket.drawDate())
                    .ticketId(savedTicket.tickedId())
                    .numbersFromUser(numbersFromUser)
                    .message("success")
                    .build();
        }
        return InputNumberResultDto.builder().message("failed").build();
    }

    public List<TicketDto> userNumbers(LocalDateTime date) {
        List<Ticket> allTicketsByDrawDate = repository.findTicketByDrawDate(date);
        return allTicketsByDrawDate.stream()
                .map(TicketMapper::mapFromTicket)
                .toList();


    }


}
