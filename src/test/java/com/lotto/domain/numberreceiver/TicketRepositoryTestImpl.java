package com.lotto.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class TicketRepositoryTestImpl implements TicketRepository {

    Map<String, Ticket> inMemoryDataBase = new ConcurrentHashMap<>();

    @Override
    public Ticket save(final Ticket ticket) {
        inMemoryDataBase.put(ticket.hash(),ticket);
        return ticket;
    }

    @Override
    public Collection<Ticket> findAllTicketByDrawDate(final LocalDateTime date) {
        return inMemoryDataBase.values()
                .stream()
                .filter(ticket -> ticket.drawDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public Ticket findByHash(final String hash) {
        return inMemoryDataBase.get(hash);
    }
}
