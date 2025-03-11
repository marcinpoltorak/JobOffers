package pl.joboffers.domain.offer;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import pl.joboffers.domain.offer.dto.JobOfferResponse;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
public class OfferFacadeTest {

    @Test
    public void should_fetch_from_jobs_from_remote_and_save_all_offers_when_repository_is_empty(){
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration().offerFacadeForTests();
        assertThat(offerFacade.findAllOffers()).isEmpty();
        // when
        List<OfferResponseDto> offers = offerFacade.fetchAllOffersAndSaveAllIfNotExist();
        // then
        assertThat(offers).hasSize(6);
    }

    @Test
    public void should_save_only_2_offers_when_repository_had_4_added_with_offer_urls(){
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(
                List.of(
                        new JobOfferResponse("asf", "asf", "5000", "1"),
                        new JobOfferResponse("assgf", "asjhf", "5800", "2"),
                        new JobOfferResponse("asfgd", "asvbnf", "6000", "3"),
                        new JobOfferResponse("ashgf", "as vnf", "7000", "4")
                )
        ).offerFacadeForTests();
        offerFacade.saveOffer(new OfferRequestDto("asf", "asf", "5000", "1"));
        offerFacade.saveOffer(new OfferRequestDto("assgf", "asjhf", "5800", "2"));
        offerFacade.saveOffer(new OfferRequestDto("asdfssgf", "asjsfdhf", "6700", "5"));
        offerFacade.saveOffer(new OfferRequestDto("assgsdff", "assdfjhf", "6200", "6"));
        assertThat(offerFacade.findAllOffers()).hasSize(4);
        // when
        List<OfferResponseDto> response = offerFacade.fetchAllOffersAndSaveAllIfNotExist();

        // then
        assertThat(List.of(
                response.get(0).offerUrl(),
                response.get(1).offerUrl()
        )).containsExactlyInAnyOrder("3", "4");
    }

    @Test
    public void should_find_offer_by_id_when_offer_was_saved(){
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration().offerFacadeForTests();
        OfferResponseDto offerResponse = offerFacade.saveOffer(new OfferRequestDto("hh", "re", "6000", "https://mydomain.net/1"));
        // when
        OfferResponseDto offerById = offerFacade.findOfferById(offerResponse.id());
        // then
        assertThat(offerById).isEqualTo(
                OfferResponseDto.builder()
                        .id(offerById.id())
                        .company("hh")
                        .position("re")
                        .salary("6000")
                        .offerUrl("https://mydomain.net/1")
                        .build()
        );
    }

    @Test
    public void should_throw_not_found_exception_when_offer_now_found(){
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        assertThat(offerFacade.findAllOffers()).isEmpty();
        // when
        Throwable thrown = catchThrowable(() -> offerFacade.findOfferById("2"));
        // then
        assertThat(thrown)
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessage("Offer with id 2 not found");
    }

    @Test
    public void should_throw_duplicate_key_exception_when_with_offer_url_exists(){
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        OfferResponseDto savedOffer = offerFacade.saveOffer(new OfferRequestDto("asd", "gfd", "1345", "myjob.pl"));
        assertThat(offerFacade.findOfferById(savedOffer.id())).isEqualTo(savedOffer);
        // when
        Throwable thrown = catchThrowable(() -> offerFacade.saveOffer(
                new OfferRequestDto("gfd", "hjgf", "1354", "myjob.pl")));
        // then
        assertThat(thrown)
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessage("Offer with offerUrl [myjob.pl] already exists");
    }
}
