package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import java.util.List;

@AllArgsConstructor
public class OfferFacade {
    private final OfferRepository offerRepository;
    private final OfferService offerService;

    @Cacheable("jobOffers")
    public List<OfferResponseDto> findAllOffers(){
        return offerRepository.findAll()
                .stream().map(
                        OfferMapper::mapFromOfferToOfferDto
                ).toList();
    }

    public List<OfferResponseDto> fetchAllOffersAndSaveAllIfNotExist(){
        return offerService.fetchAllOffersAndSaveAllIfNotExist()
                .stream()
                .map(OfferMapper::mapFromOfferToOfferDto)
                .toList();
    }

    public OfferResponseDto findOfferById(String id){
        return offerRepository.findById(id)
                .map(OfferMapper::mapFromOfferToOfferDto)
                .orElseThrow(() -> new OfferNotFoundException(id));
    }

    public OfferResponseDto saveOffer(OfferRequestDto offerDto){
        final Offer offer = OfferMapper.mapFromOfferDtoToOffer(offerDto);
        final Offer save = offerRepository.save(offer);
        return OfferMapper.mapFromOfferToOfferDto(save);
    }
}
