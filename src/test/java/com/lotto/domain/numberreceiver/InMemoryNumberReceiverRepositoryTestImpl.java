package com.lotto.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryNumberReceiverRepositoryTestImpl implements NumberReceiverRepository {

    Map<String, Ticket> inMemoryDataBase = new ConcurrentHashMap<>();

    @Override
    public Ticket save(final Ticket ticket) {
        inMemoryDataBase.put(ticket.tickedId(),ticket);
        return ticket;
    }

    @Override
    public List<Ticket> findTicketByDrawDate(final LocalDateTime date) {
        return inMemoryDataBase.values()
                .stream()
                .filter(ticket -> ticket.drawDate().equals(date))
                .toList();
    }
}
