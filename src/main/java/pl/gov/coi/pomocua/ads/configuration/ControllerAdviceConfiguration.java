package pl.gov.coi.pomocua.ads.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import pl.gov.coi.pomocua.ads.error.BaseErrorResponse;
import pl.gov.coi.pomocua.ads.error.ValidationError;
import pl.gov.coi.pomocua.ads.error.ValidationErrorResponse;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerAdviceConfiguration {
    @InitBinder
    private void initDirectFieldAccess(DataBinder dataBinder) {
        dataBinder.initDirectFieldAccess();
    }

    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity<ValidationErrorResponse> handle(HttpServletRequest request, BindException e) {

        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();

        HttpStatus status = HttpStatus.BAD_REQUEST;
        buildErrorResponse(validationErrorResponse, status);

        for (ObjectError globalError : e.getGlobalErrors()) {
            String field = globalError.getObjectName();
            String message = StringUtils.capitalize(globalError.getDefaultMessage());

            validationErrorResponse.getErrors().add(new ValidationError(field, message));
        }

        for (FieldError fieldError : e.getFieldErrors()) {

            String field = fieldError.getField();
            String message = StringUtils.capitalize(fieldError.getDefaultMessage());
            validationErrorResponse.getErrors().add(new ValidationError(field, message));
        }

        return ResponseEntity.status(status).body(validationErrorResponse);
    }

    private void buildErrorResponse(BaseErrorResponse errorResponse, HttpStatus status) {
        errorResponse.setStatus(status.value());
        errorResponse.setError(status.getReasonPhrase());
    }
}
