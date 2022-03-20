package pl.gov.coi.pomocua.ads.captcha;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.gov.coi.pomocua.ads.error.CodeException;
import pl.gov.coi.pomocua.ads.error.CodeValidationError;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CaptchaException extends CodeException {
    public CaptchaException(CodeValidationError validationError) {
        super(validationError);
    }
}
