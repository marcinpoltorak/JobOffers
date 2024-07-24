package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponse;

import java.util.List;

public interface OfferFetchable {
    List<JobOfferResponse> fetchOffers();
}
