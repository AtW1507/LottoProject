package com.lotto.domain.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Collection;

interface TicketRepository extends MongoRepository<Ticket, String> {

    Collection<Ticket> findAllTicketByDrawDate(LocalDateTime date);

    Ticket findByHash(String hash);
}


