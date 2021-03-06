package pl.gov.coi.pomocua.ads;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.gov.coi.pomocua.ads.error.CodeException;
import pl.gov.coi.pomocua.ads.error.CodeValidationError;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OfferNotFoundException extends CodeException {

    public OfferNotFoundException() {
        super(CodeValidationError.objectError("offer", "offer.not-found"));
    }
}
