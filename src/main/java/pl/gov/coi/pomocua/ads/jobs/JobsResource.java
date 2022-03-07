package pl.gov.coi.pomocua.ads.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;

import javax.validation.Valid;

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

    @GetMapping("jobs")
    public Page<JobOffer> list(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }

    @GetMapping("jobs/{id}")
    public ResponseEntity<JobOffer> get(@PathVariable Long id) {
        return ResponseEntity.of(repository.findById(id));
    }
}
