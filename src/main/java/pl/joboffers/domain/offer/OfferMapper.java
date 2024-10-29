package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponse;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import java.util.List;

public class OfferMapper {
    public static OfferResponseDto mapFromOfferToOfferDto(Offer offer){
        return OfferResponseDto.builder()
                .id(offer.id())
                .position(offer.position())
                .company(offer.company())
                .salary(offer.salary())
                .offerUrl(offer.offerUrl()).build();
    }

    public static Offer mapFromOfferDtoToOffer(OfferRequestDto offerDto){
        return Offer.builder()
                .position(offerDto.position())
                .company(offerDto.company())
                .salary(offerDto.salary())
                .offerUrl(offerDto.offerUrl()).build();
    }

    public static Offer mapFromJobOfferResponseToOffer(JobOfferResponse response){
        return Offer.builder()
                .company(response.company())
                .salary(response.salary())
                .offerUrl(response.offerUrl())
                .build();
    }
}
