package pl.gov.coi.pomocua.ads.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationError {

    private String field;

    private String message;
}
