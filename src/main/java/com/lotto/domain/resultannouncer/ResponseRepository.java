package com.lotto.domain.resultannouncer;

import java.util.Optional;

public interface ResponseRepository {
    ResultResponse save(ResultResponse resultResponse);

    boolean existById(String hash);

    Optional<ResultResponse> findById(String hash);
}
