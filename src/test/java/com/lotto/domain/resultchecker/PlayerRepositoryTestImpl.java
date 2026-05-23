package com.lotto.domain.resultchecker;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class PlayerRepositoryTestImpl implements PlayerRepository{
    Map<String, Player> playerList = new ConcurrentHashMap<>();

    @Override
    public List<Player> saveAll(final List<Player> players) {
        players.forEach(player -> playerList.put(player.hash(), player));
        return players;
    }

    @Override
    public Optional<Player> findById(final String hash) {
        return Optional.ofNullable(playerList.get(hash));
    }
}
