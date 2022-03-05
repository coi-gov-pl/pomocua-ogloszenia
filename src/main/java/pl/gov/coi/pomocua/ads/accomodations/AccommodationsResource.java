package pl.gov.coi.pomocua.ads.accomodations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccommodationsResource {
    private final AccommodationsRepository repository;

    @PostMapping("secure/accommodations")
    @ResponseStatus(HttpStatus.CREATED)
    public AccommodationOffer create(@Valid @RequestBody AccommodationOffer offer) {
        offer.id = null;
        return repository.save(offer);
    }

    @GetMapping("accommodations")
    public Page<AccommodationOffer> list(@RequestParam(required = false, defaultValue = "1") Integer capacity, Pageable pageRequest) {
        return repository.findAllByGuestsIsGreaterThanEqual(capacity, pageRequest);
    }

    @GetMapping("accommodations/{region}/{city}")
    public Page<AccommodationOffer> list(@PathVariable String region, @PathVariable String city, @RequestParam(defaultValue = "1") Integer capacity, Pageable pageRequest) {
        return repository.findAllByLocation_RegionIgnoreCaseAndLocation_CityIgnoreCaseAndGuestsIsGreaterThanEqual(region, city, capacity, pageRequest);
    }

    @GetMapping("accommodations/{id}")
    public Optional<AccommodationOffer> list(@PathVariable Long id) {
        return repository.findById(id);
    }
}
