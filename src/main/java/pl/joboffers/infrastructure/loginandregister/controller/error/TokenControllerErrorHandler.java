package pl.joboffers.infrastructure.loginandregister.controller.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.joboffers.infrastructure.offer.controller.error.OfferPostErrorResponse;

import java.util.Collections;

@ControllerAdvice("pl.joboffers.infrastructure.loginandregister")
@Log4j2
public class TokenControllerErrorHandler {

    private static final String BAD_CREDENTIALS = "Bad Credentials";

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public TokenErrorResponse handleBadCredentials() {
        return new TokenErrorResponse(BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public OfferPostErrorResponse handleAccountAlreadyExists(DuplicateKeyException exception){
        String message = "Account already exists";
        log.warn(message);
        return new OfferPostErrorResponse(Collections.singletonList(message), HttpStatus.CONFLICT);
    }
}
