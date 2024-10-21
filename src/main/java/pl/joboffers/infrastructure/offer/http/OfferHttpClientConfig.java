package pl.joboffers.infrastructure.offer.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.joboffers.domain.offer.OfferFetchable;

import java.time.Duration;

@Configuration
public class OfferHttpClientConfig {

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler(){
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateResponseErrorHandler restTemplateResponseErrorHandler){
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(1000))
                .setReadTimeout(Duration.ofMillis(1000))
                .build();
    }

    @Bean
    public OfferFetchable remoteOfferClient(RestTemplate restTemplate,
                                                   @Value("${job-offers.offer-fetcher.http.client.config.uri}") String uri,
                                                   @Value("${job-offers.offer-fetcher.http.client.config.port}") int port
                                                   ){
        return new OfferHttpClient(restTemplate, uri, port);
    }
}
