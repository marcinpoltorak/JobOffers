package pl.joboffers.domain.offer;

import lombok.Builder;

@Builder
record Offer(
        String id,
        String title,
        String position,
        String company,
        String salary,
        String offerUrl
) {
}
