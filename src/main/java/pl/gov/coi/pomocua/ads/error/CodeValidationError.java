package pl.gov.coi.pomocua.ads.error;

import lombok.Data;

@Data
public class CodeValidationError {

    private String field;

    private String messageCode;

    private ValidationError.Type type;

    public CodeValidationError(String field, String messageCode, ValidationError.Type type) {
        this.field = field;
        this.messageCode = messageCode;
        this.type = type;
    }

    public static CodeValidationError fieldError(String field, String messageCode) {
        return new CodeValidationError(field, messageCode, ValidationError.Type.FIELD);
    }

    public static CodeValidationError objectError(String field, String messageCode) {
        return new CodeValidationError(field, messageCode, ValidationError.Type.OBJECT);
    }
}
