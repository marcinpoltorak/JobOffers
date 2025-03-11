package pl.joboffers.domain.offer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfferRepository extends MongoRepository<Offer, String> {
    boolean existsByOfferUrl(String offerUrl);
}
