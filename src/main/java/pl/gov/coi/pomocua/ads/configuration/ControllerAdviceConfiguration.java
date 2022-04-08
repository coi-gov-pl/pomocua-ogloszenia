package pl.gov.coi.pomocua.ads.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.gov.coi.pomocua.ads.error.CodeException;
import pl.gov.coi.pomocua.ads.error.CodeValidationError;
import pl.gov.coi.pomocua.ads.error.ValidationError;
import pl.gov.coi.pomocua.ads.error.ErrorResponse;
import pl.gov.coi.pomocua.ads.mail.MessageProvider;

import java.util.Optional;

@RequiredArgsConstructor
@ControllerAdvice
public class ControllerAdviceConfiguration {

    private final MessageProvider messageProvider;

    @InitBinder
    private void initDirectFieldAccess(DataBinder dataBinder) {
        dataBinder.initDirectFieldAccess();
    }

    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity<ErrorResponse> handle(BindException e) {

        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST);
        e.getGlobalErrors().forEach(error -> response.addError(new ValidationError(error)));
        e.getFieldErrors().stream()
                .filter(fieldError -> !fieldError.isBindingFailure())
                .forEach(error -> response.addError(new ValidationError(error)));

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(value = {CodeException.class})
    public ResponseEntity<ErrorResponse> handle(CodeException e) {

        ErrorResponse response = new ErrorResponse(resolveHttpStatus(e));

        CodeValidationError validationError = e.getValidationError();
        String message = messageProvider.getMessageByCodeAndLocale(validationError.getMessageCode(), LocaleContextHolder.getLocale());
        response.addError(new ValidationError(validationError.getField(), message, validationError.getType()));

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    private HttpStatus resolveHttpStatus(Exception e) {
        ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(e.getClass(), ResponseStatus.class);
        return Optional.ofNullable(annotation).map(ResponseStatus::code).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
