package com.lotto.infrastructure.numbergenerator.http;

import com.lotto.domain.numbergenerator.RandomNumberGenerable;
import com.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


@AllArgsConstructor
@Log4j2
class RandomNumberGeneratorRestTemplate implements RandomNumberGenerable {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;


    @Override
    public SixRandomNumbersDto generateSixRandomNumbers(int count, int lowerBand, int upperBand) {
        log.info("Started fetching winning numbers using http client");
        HttpHeaders headers = new HttpHeaders();
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        try {
            final ResponseEntity<SixRandomNumbersDto> response = makeGetRequest(count, lowerBand, upperBand, requestEntity);
            Set<Integer> sixDistinctNumbers = getSixRandomDistinctNumbers(response);
            if (sixDistinctNumbers.size() != 6) {
                log.error("Set is less than: {} Have to request one more time", count);
                return generateSixRandomNumbers(count, lowerBand, upperBand);
            }
            return SixRandomNumbersDto.builder()
                    .numbers(sixDistinctNumbers)
                    .build();
        } catch (ResourceAccessException e) {
            log.error("Error while fetching winning numbers using http client: " + e.getMessage());
            return SixRandomNumbersDto.builder().build();
        }
    }

    private ResponseEntity<SixRandomNumbersDto> makeGetRequest(int count, int lowerBand, int upperBand, HttpEntity<HttpHeaders> requestEntity) {
        final String url = UriComponentsBuilder.fromHttpUrl(getUrlForService("/v1/integer"))
                .queryParam("min", lowerBand)
                .queryParam("max", upperBand)
                .queryParam("count", count)
                .toUriString();
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<SixRandomNumbersDto>() {}
        );
    }

    private Set<Integer> getSixRandomDistinctNumbers(ResponseEntity<SixRandomNumbersDto> response) {
        SixRandomNumbersDto body = response.getBody();
        if (body == null) {
            log.error("Response Body was null returning empty collection");
            return Collections.emptySet();
        }
        log.info("Success Response Body Returned: " + response);

        return body.numbers().stream()
                .limit(6)
                .collect(Collectors.toSet());
    }

    private String getUrlForService(String service) {
        if (port == 80 || port == 443) {
            return uri + service;

        }
        return uri + ":" + port + service;
    }
}
