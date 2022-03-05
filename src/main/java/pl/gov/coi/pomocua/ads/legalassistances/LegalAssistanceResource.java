package pl.gov.coi.pomocua.ads.legalassistances;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping(path = "/api/", produces = "application/json")
@RequiredArgsConstructor
public class LegalAssistanceResource {

    private final LegalAssistanceRepository legalAssistanceRepository;

    @GetMapping(value = "legal-assistances")
    public Page<LegalAssistanceOffer> getLegalAssistances(Pageable pageRequest){
        return legalAssistanceRepository.findAll(pageRequest);
    }

    @GetMapping(value = "legal-assistances/{id}")
    public Optional<LegalAssistanceOffer> getLegalAssistance(@PathVariable Long id){
        return legalAssistanceRepository.findById(id);
    }

    @PostMapping(value = "secure/legal-assistances")
    @ResponseStatus(HttpStatus.CREATED)
    public LegalAssistanceOffer create(@Valid @RequestBody LegalAssistanceOffer legalAssistanceOffer){
        legalAssistanceOffer.id = null;
        return legalAssistanceRepository.save(legalAssistanceOffer);
    }
}
