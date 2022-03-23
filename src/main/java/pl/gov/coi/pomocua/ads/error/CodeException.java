package pl.gov.coi.pomocua.ads.error;

import lombok.Getter;

public abstract class CodeException extends RuntimeException {

    @Getter
    private final CodeValidationError validationError;

    public CodeException(CodeValidationError validationError) {
        this.validationError = validationError;
    }
}
