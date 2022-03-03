package pl.gov.coi.pomocua.ogloszenia.dictionaries.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.coi.pomocua.ogloszenia.dictionaries.domain.City;
import pl.gov.coi.pomocua.ogloszenia.dictionaries.domain.CityRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/dictionaries/cities", produces = "application/json")
public class CityLookupResource {

    private final CityRepository cityRepository;

    public CityLookupResource(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @GetMapping(value = "/{voivodeship}/{city}")
    public ResponseEntity<CityLookupResponse> getCities(@PathVariable String voivodeship, @PathVariable String city) {
        if (city.isBlank() || city.length() < 2) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<String> cities = cityRepository.findByVoivodeshipAndCityStartsWith(voivodeship, city)
                .stream()
                .map(City::getCity)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new CityLookupResponse(cities), HttpStatus.OK);
    }
}
