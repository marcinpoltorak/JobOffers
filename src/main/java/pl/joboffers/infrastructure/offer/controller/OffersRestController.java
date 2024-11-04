package pl.joboffers.infrastructure.offer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.OfferNotFoundException;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import java.util.List;

@RestController
@RequestMapping("/offers")
@AllArgsConstructor
public class OffersRestController {
    private final OfferFacade offerFacade;

    @GetMapping
    public ResponseEntity<List<OfferResponseDto>> findAllOffers(){
        List<OfferResponseDto> offers = offerFacade.findAllOffers();
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponseDto> findOfferById(@PathVariable String id){
        OfferResponseDto offer = offerFacade.findOfferById(id);
        return ResponseEntity.ok(offer);
    }
}
