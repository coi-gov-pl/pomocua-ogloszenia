package pl.gov.coi.pomocua.ads.dictionaries.api;

import pl.gov.coi.pomocua.ads.dictionaries.domain.City;

import java.util.List;

public record CityLookupResponse(List<CityLookupDto> cities) { }

record CityLookupDto(String city, String voivodeship) {

    static CityLookupDto fromEntity(City city) {
        return new CityLookupDto(city.getCity(), city.getVoivodeship());
    }
}
