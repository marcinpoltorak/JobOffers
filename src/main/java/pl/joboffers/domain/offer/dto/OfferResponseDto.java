package pl.joboffers.domain.offer.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record OfferResponseDto(
        String id,
        String company,
        String position,
        String salary,
        String offerUrl
) implements Serializable {
}
