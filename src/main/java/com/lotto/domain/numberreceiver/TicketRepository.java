package com.lotto.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.Collection;

interface TicketRepository {
    Ticket save(Ticket ticket);

    Collection<Ticket> findAllTicketByDrawDate(LocalDateTime date);

    Ticket findByHash(String hash);
}


