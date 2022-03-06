package pl.gov.coi.pomocua.ads.translations;

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

    @GetMapping("translations")
    public Page<TranslationOffer> list(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }

    @GetMapping("translations/{id}")
    public ResponseEntity<TranslationOffer> get(@PathVariable Long id) {
        return ResponseEntity.of(repository.findById(id));
    }
}
