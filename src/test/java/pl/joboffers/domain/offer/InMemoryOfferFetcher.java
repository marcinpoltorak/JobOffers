package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponse;

import java.util.List;

public class InMemoryOfferFetcher implements OfferFetchable {
    List<JobOfferResponse> listOfOffers;

    public InMemoryOfferFetcher(List<JobOfferResponse> listOfOffers) {
        this.listOfOffers = listOfOffers;
    }

    @Override
    public List<JobOfferResponse> fetchOffers() {
        return listOfOffers;
    }
}
