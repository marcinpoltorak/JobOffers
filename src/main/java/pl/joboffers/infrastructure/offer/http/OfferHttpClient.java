package pl.joboffers.infrastructure.offer.http;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import pl.joboffers.domain.offer.OfferFetchable;
import pl.joboffers.domain.offer.dto.JobOfferResponse;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Log4j2
public class OfferHttpClient implements OfferFetchable {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    @Override
    public List<JobOfferResponse> fetchOffers() {
        log.info("Started fetching offers from external service");
        HttpHeaders headers = new HttpHeaders();
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        try {
            String urlForService = getUrlForService("/offers");
            final String url = UriComponentsBuilder.fromHttpUrl(urlForService).toUriString();
            ResponseEntity<List<JobOfferResponse>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            final List<JobOfferResponse> body = response.getBody();
            if (body == null) {
                log.info("Response body null");
                throw new ResponseStatusException(HttpStatus.NO_CONTENT);
            }
            return body;
        } catch (ResourceAccessException e){
            log.error("Error occurred while trying to fetch offer from external service: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getUrlForService(String service) {
        return uri + ":" + port + service;
    }
}
