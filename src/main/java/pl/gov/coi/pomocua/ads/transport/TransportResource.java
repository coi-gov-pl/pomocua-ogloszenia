package pl.gov.coi.pomocua.ads.transport;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.OfferNotFoundException;
import pl.gov.coi.pomocua.ads.Offers;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;

import javax.validation.Valid;

import static pl.gov.coi.pomocua.ads.Offers.page;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransportResource {
    private final TransportOfferRepository repository;
    private final CurrentUser currentUser;

    @PostMapping("secure/transport")
    @ResponseStatus(HttpStatus.CREATED)
    public TransportOffer create(@Valid @RequestBody TransportOfferDefinitionDTO offerDefinition) {
        TransportOffer offer = new TransportOffer();
        offerDefinition.applyTo(offer);
        offer.userId = currentUser.getCurrentUserId();
        return repository.save(offer);
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

    @PutMapping("secure/transport/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody TransportOfferDefinitionDTO update) {
        TransportOffer offer = repository.findByIdAndUserId(id, currentUser.getCurrentUserId())
                .orElseThrow(OfferNotFoundException::new);

        update.applyTo(offer);

        repository.save(offer);
    }
}
