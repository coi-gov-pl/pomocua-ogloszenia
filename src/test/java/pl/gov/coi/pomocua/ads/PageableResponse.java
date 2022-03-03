package pl.gov.coi.pomocua.ogloszenia;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PageableResponse<T> {
    public T[] content;
}
