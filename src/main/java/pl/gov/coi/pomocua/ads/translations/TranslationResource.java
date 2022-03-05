package pl.gov.coi.pomocua.ads.translations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TranslationResource {

    private final TranslationOfferRepository repository;

    @PostMapping("secure/translations")
    @ResponseStatus(HttpStatus.CREATED)
    public TranslationOffer create(@Valid @RequestBody TranslationOffer translationOffer) {
        translationOffer.id = null;
        return repository.save(translationOffer);
    }

    @GetMapping("translations")
    public Page<TranslationOffer> list(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }
    @GetMapping("translations/{id}")
    public Optional<TranslationOffer> list(@PathVariable Long id) {
        return repository.findById(id);
    }
}
