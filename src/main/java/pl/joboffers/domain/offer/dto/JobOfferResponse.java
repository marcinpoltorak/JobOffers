package pl.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record JobOfferResponse(
        String title,
        String company,
        String salary,
        String offerUrl
) {
}
