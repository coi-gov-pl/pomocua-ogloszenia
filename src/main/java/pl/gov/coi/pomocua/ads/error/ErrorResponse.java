package pl.gov.coi.pomocua.ads.error;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ErrorResponse {

    private int status;

    private String error;

    private List<ValidationError> errors;

    public ErrorResponse(HttpStatus status) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.errors = new ArrayList<>();
    }

    public void addError(ValidationError validationError) {
        this.errors.add(validationError);
    }
}
