package pl.gov.coi.pomocua.ads.assistance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping(path = "/api/", produces = "application/json")
public class LegalAssistanceResource {

    private final LegalAssistanceRepository legalAssistanceRepository;

    public LegalAssistanceResource(LegalAssistanceRepository legalAssistanceRepository){this.legalAssistanceRepository = legalAssistanceRepository;}

    @GetMapping(value = "legal_assistances")
    public Page<LegalAssistanceOffer> getLegalAssistances(Pageable pageRequest){
        return legalAssistanceRepository.findAll(pageRequest);
    }

    @GetMapping(value = "legal_assistances/{id}")
    public Optional<LegalAssistanceOffer> getLegalAssistance(@PathVariable Long id){
        return legalAssistanceRepository.findById(id);
    }

    @PostMapping(value = "secure/legal_assistances")
    @ResponseStatus(HttpStatus.CREATED)
    public LegalAssistanceOffer create(@Valid @RequestBody LegalAssistanceOffer legalAssistanceOffer){
        legalAssistanceOffer.id = null;
        return legalAssistanceRepository.save(legalAssistanceOffer);
    }
}
