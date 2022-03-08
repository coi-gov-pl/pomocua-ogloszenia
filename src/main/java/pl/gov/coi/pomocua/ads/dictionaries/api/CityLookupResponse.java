package pl.gov.coi.pomocua.ads.dictionaries.api;

import pl.gov.coi.pomocua.ads.dictionaries.domain.City;

import javax.validation.constraints.NotNull;
import java.util.List;

public record CityLookupResponse(List<CityLookupDto> cities) {
}

record CityLookupDto(@NotNull String city, @NotNull String region) {

    static CityLookupDto fromEntity(City city) {
        return new CityLookupDto(
                city.getLocation().getCity(),
                city.getLocation().getRegion()
        );
    }
}
