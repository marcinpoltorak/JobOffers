package pl.joboffers.http.error;

import org.springframework.web.client.RestTemplate;
import pl.joboffers.domain.offer.OfferFetchable;
import pl.joboffers.infrastructure.offer.http.OfferHttpClient;
import pl.joboffers.infrastructure.offer.http.OfferHttpClientConfig;

public class OfferRestTemplateIntegrationTestConfig extends OfferHttpClientConfig {

    public OfferFetchable remoteOfferClient(int port){
        RestTemplate restTemplate = restTemplate(restTemplateResponseErrorHandler());
        return new OfferHttpClient(restTemplate, "http://localhost", port);
    }
}
