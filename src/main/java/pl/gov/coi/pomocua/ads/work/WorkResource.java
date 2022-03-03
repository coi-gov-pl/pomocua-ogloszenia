package pl.gov.coi.pomocua.ads.work;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class WorkResource {
    private final WorkOfferRepository repository;

    @PostMapping("/api/secure/offer/work")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkOffer create(@Valid @RequestBody WorkOffer workOffer) {
        workOffer.id = null;
        return repository.save(workOffer);
    }

    @GetMapping("/api/offer/work")
    public Page<WorkOffer> list(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }
    @GetMapping("/api/offer/work/{id}")
    public Optional<WorkOffer> list(@PathVariable Long id) {
        return repository.findById(id);
    }
}
