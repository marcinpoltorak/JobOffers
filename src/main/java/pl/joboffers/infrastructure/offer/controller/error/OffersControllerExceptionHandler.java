package pl.joboffers.infrastructure.offer.controller.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.joboffers.domain.offer.OfferNotFoundException;

@ControllerAdvice
@Log4j2
public class OffersControllerExceptionHandler {

    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public OfferErrorResponse handleOfferNotFound(OfferNotFoundException exception){
        String message = exception.getMessage();
        log.error(message);
        return new OfferErrorResponse(message, HttpStatus.NOT_FOUND);
    }

}
