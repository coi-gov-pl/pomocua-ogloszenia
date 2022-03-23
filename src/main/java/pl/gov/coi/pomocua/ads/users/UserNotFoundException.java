package pl.gov.coi.pomocua.ads.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.gov.coi.pomocua.ads.error.CodeException;
import pl.gov.coi.pomocua.ads.error.CodeValidationError;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends CodeException {

    public UserNotFoundException() {
        super(CodeValidationError.objectError("user", "user.not-found"));
    }
}