package pl.gov.coi.pomocua.ads.mail;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class HtmlGenerationException extends RuntimeException {
}
