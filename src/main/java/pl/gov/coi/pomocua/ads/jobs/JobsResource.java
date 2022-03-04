package pl.gov.coi.pomocua.ads.jobs;

import lombok.AllArgsConstructor;
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
public class JobsResource {
    private final JobsRepository repository;

    @PostMapping("secure/jobs")
    @ResponseStatus(HttpStatus.CREATED)
    public JobOffer create(@Valid @RequestBody JobOffer workOffer) {
        workOffer.id = null;
        return repository.save(workOffer);
    }

    @GetMapping("jobs")
    public Page<JobOffer> list(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }

    @GetMapping("jobs/{id}")
    public Optional<JobOffer> list(@PathVariable Long id) {
        return repository.findById(id);
    }
}
