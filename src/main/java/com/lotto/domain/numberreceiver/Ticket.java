package com.lotto.domain.numberreceiver;
//encja do bazy

import java.time.LocalDateTime;
import java.util.Set;

record Ticket(String tickedId, LocalDateTime drawDate, Set<Integer> numbersFromUser) {
}
