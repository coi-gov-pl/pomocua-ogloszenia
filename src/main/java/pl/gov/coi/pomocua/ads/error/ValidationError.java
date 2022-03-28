package pl.gov.coi.pomocua.ads.error;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Data
@NoArgsConstructor
public class ValidationError {

    private String field;

    private String message;

    private Type type;

    public ValidationError(FieldError error) {
        this(error.getField(), error.getDefaultMessage(), Type.FIELD);
    }

    public ValidationError(ObjectError error) {
        this(error.getObjectName(), error.getDefaultMessage(), Type.OBJECT);
    }

    public ValidationError(String field, String message, Type type) {
        this.field = field;
        this.message = message;
        this.type = type;
    }

    public enum Type {
        FIELD,
        OBJECT
    }
}
