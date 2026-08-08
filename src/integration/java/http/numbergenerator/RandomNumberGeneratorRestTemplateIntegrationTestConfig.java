package http.numbergenerator;

import com.lotto.domain.numbergenerator.RandomNumberGenerable;
import com.lotto.infrastructure.numbergenerator.http.RandomGeneratorClientConfig;
import org.springframework.web.client.RestTemplate;

public class RandomNumberGeneratorRestTemplateIntegrationTestConfig extends RandomGeneratorClientConfig {

    public RandomNumberGenerable remoteNumberGeneratorClient(int port, int connectionTimeout, int readTimeout){
        RestTemplate restTemplate = restTemplate(connectionTimeout, readTimeout,restTemplateResponseErrorHandler());
        return remoteNumberGeneratorClient(restTemplate, "http://localhost", port);
    }

}
