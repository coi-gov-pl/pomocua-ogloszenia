package pl.gov.coi.pomocua.ads.dictionaries.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.coi.pomocua.ads.dictionaries.domain.LegalAssistance;
import pl.gov.coi.pomocua.ads.dictionaries.domain.LegalAssistanceRepository;


@RestController
@RequestMapping(path = "/api/legal-assistances", produces = "application/json")
public class LegalAssistanceResource {

    private final LegalAssistanceRepository legalAssistanceRepository;

    public LegalAssistanceResource(LegalAssistanceRepository legalAssistanceRepository){this.legalAssistanceRepository = legalAssistanceRepository}

    @GetMapping(value = "/")
    public Page<LegalAssistance> getLegalAssistances(Pageable pageRequest){
        return legalAssistanceRepository.findAll(pageRequest);
    }

}
