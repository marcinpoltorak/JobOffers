package pl.joboffers.infrastructure.offer.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
@Log4j2
public class HttpOffersScheduler {

    private final OfferFacade offerFacade;
    private static final String STARTED_FETCHING_OFFERS_MESSAGE = "Started fetching offers {}";
    private static final String STOPPED_FETCHING_OFFERS_MESSAGE = "Stopped fetching offers {}";
    private static final String ADDED_NEW_OFFERS_MESSAGE = "Added {} new offers";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedDelayString = "${offers.http.scheduler.request.delay}")
    public List<OfferResponseDto> fetchAllOffersAndSaveAllIfNotExist(){
        log.info(STARTED_FETCHING_OFFERS_MESSAGE, dateFormat.format(new Date()));
        List<OfferResponseDto> offerResponseDtoList = offerFacade.fetchAllOffersAndSaveAllIfNotExist();
        log.info(ADDED_NEW_OFFERS_MESSAGE, offerResponseDtoList.size());
        log.info(STOPPED_FETCHING_OFFERS_MESSAGE, dateFormat.format(new Date()));
        return offerResponseDtoList;
    }
}
