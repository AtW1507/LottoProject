package com.lotto.domain.resultchecker;

import java.util.List;
import java.util.Optional;

interface PlayerRepository {

    List<Player> saveAll(List<Player> players);

    Optional<Player> findById(String hash);
}
