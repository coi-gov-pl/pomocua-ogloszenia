package pl.gov.coi.pomocua.ads.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Offers;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;

import javax.validation.Valid;

import java.util.Optional;

import static pl.gov.coi.pomocua.ads.Offers.page;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobsResource {
    private final JobsRepository repository;
    private final CurrentUser currentUser;

    @PostMapping("secure/jobs")
    @ResponseStatus(HttpStatus.CREATED)
    public JobOffer create(@Valid @RequestBody JobOffer workOffer) {
        workOffer.id = null;
        workOffer.userId = currentUser.getCurrentUserId();
        return repository.save(workOffer);
    }

    @DeleteMapping("secure/jobs/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Optional<JobOffer> jobOffer = repository.findById(id);
        jobOffer.ifPresent(offer -> {
            offer.status = BaseOffer.Status.INACTIVE;
            repository.save(offer);
        });
    }

    @GetMapping("jobs")
    public Offers<JobOffer> list(Pageable pageRequest) {
        return page(repository.findAll(pageRequest));
    }

    @GetMapping("jobs/{id}")
    public ResponseEntity<JobOffer> get(@PathVariable Long id) {
        return ResponseEntity.of(repository.findById(id));
    }
}
