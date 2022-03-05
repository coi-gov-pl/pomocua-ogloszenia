package pl.gov.coi.pomocua.ads.dictionaries.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.coi.pomocua.ads.dictionaries.domain.CityRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/dictionaries/city", produces = MediaType.APPLICATION_JSON_VALUE)
public class CityLookupResource {

    private final CityRepository cityRepository;

    public CityLookupResource(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public ResponseEntity<CityLookupResponse> getCities(@RequestParam String query) {
        if (!StringUtils.hasText(query) || query.length() < 2) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<CityLookupDto> cities = cityRepository
                .findFirst5ByLocationCityStartsWithOrderByLocationCityAsc(query.toLowerCase())
                .stream()
                .map(CityLookupDto::fromEntity)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new CityLookupResponse(cities), HttpStatus.OK);
    }
}
