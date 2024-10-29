package pl.joboffers.domain.offer;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class InMemoryOfferRepository implements OfferRepository {
    Map<String, Offer> offers = new ConcurrentHashMap<>();

    @Override
    public <S extends Offer> @NotNull List<S> saveAll(Iterable<S> entities) {
        List<S> entitiesList = new ArrayList<>();
        entities.iterator().forEachRemaining(entity -> {
            entitiesList.add(entity);
            save(entity);
        });
        return entitiesList;
    }

    @Override
    public @NotNull List<Offer> findAll() {
        return offers.values().stream().toList();
    }

    @Override
    public Iterable<Offer> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Offer entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Offer> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Offer> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Offer> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Offer> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Offer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Offer> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Offer> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Offer> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Offer, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public boolean existsByOfferUrl(String offerUrl) {
        return offers.values()
                .stream()
                .filter(offer -> offer.offerUrl().equals(offerUrl))
                .count() == 1;
    }

    @Override
    public Optional<Offer> findById(String id) {
        return Optional.ofNullable(offers.get(id));
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public <S extends Offer> S save(S entity) {
        if(offers.values().stream().anyMatch(offer -> offer.offerUrl().equals(entity.offerUrl()))){
            throw new OfferDuplicateException(entity.offerUrl());
        }
        UUID id = UUID.randomUUID();
        Offer offer = new Offer(
                id.toString(),
                entity.position(),
                entity.company(),
                entity.salary(),
                entity.offerUrl()
        );
        offers.put(offer.id(), offer);
        return (S) offer;
    }


}
