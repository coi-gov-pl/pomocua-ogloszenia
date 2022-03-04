package pl.gov.coi.pomocua.ads.transport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/")
public class TransportResource {

    private final TransportOfferRepository repository;

    public TransportResource(TransportOfferRepository transportOfferRepository) {
        this.repository = transportOfferRepository;
    }

    @PostMapping("secure/transport")
    @ResponseStatus(HttpStatus.CREATED)
    public TransportOffer create(@Valid @RequestBody TransportOffer offer) {
        offer.id = null;
        return repository.save(offer);
    }

    @GetMapping("transport")
    public Page<TransportOffer> list(Pageable pageRequest, TransportOfferSearchCriteria searchCriteria) {
        return repository.findAll(TransportOfferSpecifications.from(searchCriteria), pageRequest);
    }

    @GetMapping("transport/{id}")
    public Optional<TransportOffer> list(@PathVariable Long id) {
        return repository.findById(id);
    }
}
