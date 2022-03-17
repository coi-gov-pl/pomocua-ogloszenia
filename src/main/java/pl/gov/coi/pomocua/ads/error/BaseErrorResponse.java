package pl.gov.coi.pomocua.ads.error;

import lombok.Data;

@Data
public class BaseErrorResponse {

    private int status;

    private String error;
}
