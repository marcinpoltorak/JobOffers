package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.OfferResponseDto;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {

    List<Offer> findAll();

    boolean existsByOfferUrl(String offerUrl);

    List<Offer> saveAll(List<Offer> offers);

    Optional<Offer> findById(String id);

    Offer save(Offer offer);
}
