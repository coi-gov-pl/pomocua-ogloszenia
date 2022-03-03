package pl.gov.coi.pomocua.ads.dictionaries.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.coi.pomocua.ads.dictionaries.domain.City;
import pl.gov.coi.pomocua.ads.dictionaries.domain.CityRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/dictionaries/city", produces = "application/json")
public class CityLookupResource {

    private final CityRepository cityRepository;

    public CityLookupResource(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @GetMapping(value = "/")
    public ResponseEntity<CityLookupResponse> getCities(@RequestParam String region, @RequestParam String city) {
        if (!StringUtils.hasText(city) || city.length() < 2 || !StringUtils.hasText(region)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<String> cities = cityRepository.findByVoivodeshipAndCityStartsWith(region.toLowerCase(), city.toLowerCase())
                .stream()
                .map(City::getCity)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new CityLookupResponse(cities), HttpStatus.OK);
    }
}
