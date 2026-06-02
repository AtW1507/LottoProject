package com.lotto.domain.resultannouncer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class ResponseRepositoryTestImpl implements ResponseRepository{

    private final Map<String, ResultResponse> responseList = new ConcurrentHashMap<>();

    @Override
    public ResultResponse save(ResultResponse resultResponse) {
        return responseList.put(resultResponse.hash(),resultResponse);
    }

    @Override
    public boolean existById(final String hash) {
        return responseList.containsKey(hash);
    }

    @Override
    public Optional<ResultResponse> findById(final String hash) {
        return Optional.ofNullable(responseList.get(hash));
    }
}
