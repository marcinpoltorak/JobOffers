package pl.joboffers.domain.offer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOfferRepository implements OfferRepository {
    Map<String, Offer> offers = new ConcurrentHashMap<>();

    @Override
    public List<Offer> findAll() {
        return offers.values().stream().toList();
    }

    @Override
    public boolean existsByOfferUrl(String offerUrl) {
        return offers.values()
                .stream()
                .filter(offer -> offer.offerUrl().equals(offerUrl))
                .count() == 1;
    }

    @Override
    public List<Offer> saveAll(List<Offer> offers) {
        return offers.stream()
                .map(this::save)
                .toList();
    }

    @Override
    public Optional<Offer> findById(String id) {
        return Optional.ofNullable(offers.get(id));
    }

    @Override
    public Offer save(Offer entity) {
        if(offers.values().stream().anyMatch(offer -> offer.offerUrl().equals(entity.offerUrl()))){
            throw new OfferDuplicateException(entity.offerUrl());
        }
        UUID id = UUID.randomUUID();
        Offer offer = new Offer(
                id.toString(),
                entity.company(),
                entity.position(),
                entity.company(),
                entity.salary(),
                entity.offerUrl()
        );
        offers.put(offer.id(), offer);
        return offer;
    }
}
