package pl.gov.coi.pomocua.ads.translations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Offers;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.jobs.JobOffer;

import javax.validation.Valid;

import java.util.Optional;

import static pl.gov.coi.pomocua.ads.Offers.page;

@RestController
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TranslationResource {

    private final TranslationOfferRepository repository;
    private final CurrentUser currentUser;

    @PostMapping("secure/translations")
    @ResponseStatus(HttpStatus.CREATED)
    public TranslationOffer create(@Valid @RequestBody TranslationOffer translationOffer) {
        translationOffer.id = null;
        translationOffer.userId = currentUser.getCurrentUserId();
        return repository.save(translationOffer);
    }

    @DeleteMapping("secure/translations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Optional<TranslationOffer> translationOffer = repository.findById(id);
        translationOffer.ifPresent(offer -> {
            offer.status = BaseOffer.Status.INACTIVE;
            repository.save(offer);
        });
    }

    @GetMapping("translations")
    public Offers<TranslationOffer> list(Pageable pageRequest) {
        return page(repository.findAll(pageRequest));
    }

    @GetMapping("translations/{id}")
    public ResponseEntity<TranslationOffer> get(@PathVariable Long id) {
        return ResponseEntity.of(repository.findById(id));
    }
}
