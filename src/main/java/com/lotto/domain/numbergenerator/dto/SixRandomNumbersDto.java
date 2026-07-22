package com.lotto.domain.numbergenerator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Set;
@Builder
public record SixRandomNumbersDto(@JsonProperty("items") Set<Integer> numbers) {
}
