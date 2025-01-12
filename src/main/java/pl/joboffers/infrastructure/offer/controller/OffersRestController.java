package pl.joboffers.infrastructure.offer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import javax.validation.Valid;
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

    @PostMapping()
    public ResponseEntity<OfferResponseDto> addOffer(@RequestBody @Valid OfferRequestDto offerRequestDto){
        OfferResponseDto responseDto = offerFacade.saveOffer(offerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
