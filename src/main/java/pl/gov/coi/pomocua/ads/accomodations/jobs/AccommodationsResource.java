package pl.gov.coi.pomocua.ads.accomodations.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class AccommodationsResource {
    private final AccommodationsRepository repository;

    @PostMapping("secure/accommodations")
    @ResponseStatus(HttpStatus.CREATED)
    public AccommodationOffer create(@Valid @RequestBody AccommodationOffer offer) {
        offer.id = null;
        return repository.save(offer);
    }

    @GetMapping("accommodations")
    public Page<AccommodationOffer> list(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }

    @GetMapping("accommodations/{id}")
    public Optional<AccommodationOffer> list(@PathVariable Long id) {
        return repository.findById(id);
    }
}
