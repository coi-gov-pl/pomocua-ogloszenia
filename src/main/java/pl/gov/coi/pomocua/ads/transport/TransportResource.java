package pl.gov.coi.pomocua.ads.transport;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Offers;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.translations.TranslationOffer;

import javax.validation.Valid;

import java.util.Optional;

import static pl.gov.coi.pomocua.ads.Offers.page;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransportResource {
    private final TransportOfferRepository repository;
    private final CurrentUser currentUser;

    @PostMapping("secure/transport")
    @ResponseStatus(HttpStatus.CREATED)
    public TransportOffer create(@Valid @RequestBody TransportOffer offer) {
        offer.id = null;
        offer.userId = currentUser.getCurrentUserId();
        return repository.save(offer);
    }

    @DeleteMapping("secure/transport/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Optional<TransportOffer> transportOffer = repository.findById(id);
        transportOffer.ifPresent(offer -> {
            offer.status = BaseOffer.Status.INACTIVE;
            repository.save(offer);
        });
    }

    @Operation(description = "Allows to search for transport offers using different criterias (passes as query params). Each criteria is optional.")
    @GetMapping("transport")
    public Offers<TransportOffer> list(Pageable pageRequest, TransportOfferSearchCriteria searchCriteria) {
        return page(repository.findAll(TransportOfferSpecifications.from(searchCriteria), pageRequest));
    }

    @GetMapping("transport/{id}")
    public ResponseEntity<TransportOffer> get(@PathVariable Long id) {
        return ResponseEntity.of(repository.findById(id));
    }
}
