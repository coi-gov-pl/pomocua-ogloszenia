package pl.gov.coi.pomocua.ogloszenia.work;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class WorkResource {
    private final WorkOfferRepository repository;

    @PostMapping("/api/secure/offer/work")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkOffer create(@RequestBody WorkOffer workOffer) {
        workOffer.id = UUID.randomUUID();
        return repository.save(workOffer);
    }

    @GetMapping("/api/offer/work")
    public Page<WorkOffer> list(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }
    @GetMapping("/api/offer/work/{id}")
    public Optional<WorkOffer> list(@PathVariable UUID id) {
        return repository.findById(id);
    }
}
