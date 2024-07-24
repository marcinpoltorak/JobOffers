package pl.joboffers.domain.offer.dto;

public record OfferRequestDto(
        String company,
        String position,
        String salary,
        String offerUrl
) {
}
