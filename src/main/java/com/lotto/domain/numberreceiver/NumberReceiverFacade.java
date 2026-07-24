package com.lotto.domain.numberreceiver;

import com.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lotto.domain.numberreceiver.ValidationResult.INPUT_SUCCESS;

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
public class NumberReceiverFacade {

    private final NumberValidator numberValidator;
    private final TicketRepository ticketRepository;
    private final DrawDateGenerator drawDateGenerator;
    private final HashGenerable hashGenerator;


    public NumberReceiverResponseDto inputNumber(Set<Integer> numbersFromUser) {
        List<ValidationResult> validationResultList = numberValidator.validate(numbersFromUser);
        if (!validationResultList.isEmpty()) {
            String resultMessage = numberValidator.createResultMessage();
            return new NumberReceiverResponseDto(null, resultMessage);
        }
        LocalDateTime drawDate = drawDateGenerator.getNextDrawDate();
        String hash = hashGenerator.getHash();
        TicketDto generatedTicket = TicketDto.builder()
                .hash(hash)
                .numbers(numbersFromUser)
                .drawDate(drawDate)
                .build();


        Ticket savedTicket = Ticket.builder()
                .hash(hash)
                .numbers(generatedTicket.numbers())
                .drawDate(generatedTicket.drawDate())
                .build();
        ticketRepository.save(savedTicket);
        return new NumberReceiverResponseDto(generatedTicket, INPUT_SUCCESS.info);

    }

    public List<TicketDto> retrieveAllTicketsByNextDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        if (date.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
        return ticketRepository.findAllTicketByDrawDate(date)
                .stream()
                .filter(ticket -> ticket.drawDate().isEqual(date))
                .map(ticket -> TicketDto.builder()
                        .hash(ticket.hash())
                        .numbers(ticket.numbers())
                        .drawDate(ticket.drawDate())
                        .build())
                .collect(Collectors.toList());
    }
    public List<TicketDto> retrieveAllTicketsByNextDrawDate(){
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        return retrieveAllTicketsByNextDrawDate(nextDrawDate);
    }

    public LocalDateTime retrieveNextDrawDate() {
        return drawDateGenerator.getNextDrawDate();
    }

    public TicketDto findByHash(String hash){
        Ticket ticket = ticketRepository.findByHash(hash);
        return TicketDto.builder()
                .hash(ticket.hash())
                .numbers(ticket.numbers())
                .drawDate(ticket.drawDate())
                .build();
    }

}
