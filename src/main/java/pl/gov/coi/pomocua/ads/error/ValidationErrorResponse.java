package pl.gov.coi.pomocua.ads.error;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationErrorResponse extends BaseErrorResponse {

    private List<ValidationError> errors = new ArrayList<>();
}
