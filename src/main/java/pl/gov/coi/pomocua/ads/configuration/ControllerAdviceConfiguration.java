package pl.gov.coi.pomocua.ads.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import pl.gov.coi.pomocua.ads.error.ValidationError;
import pl.gov.coi.pomocua.ads.error.ValidationErrorResponse;

@ControllerAdvice
public class ControllerAdviceConfiguration {
    @InitBinder
    private void initDirectFieldAccess(DataBinder dataBinder) {
        dataBinder.initDirectFieldAccess();
    }

    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity<ValidationErrorResponse> handle(BindException e) {

        ValidationErrorResponse response = new ValidationErrorResponse(HttpStatus.BAD_REQUEST);
        e.getGlobalErrors().forEach(error -> response.addError(new ValidationError(error)));
        e.getFieldErrors().forEach(error -> response.addError(new ValidationError(error)));

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
