package pl.gov.coi.pomocua.ads.assistances;

import org.springframework.core.ParameterizedTypeReference;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.PageableResponse;
import pl.gov.coi.pomocua.ads.assistance.LegalAssistance;

public class LegalAssistancesResourceTest extends BaseResourceTest<LegalAssistance> {

    @Override
    protected Class<LegalAssistance> getClazz() {
        return LegalAssistance.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "legal_assistances";
    }

    @Override
    protected ParameterizedTypeReference<PageableResponse<LegalAssistance>> getResponseType() {
        return new ParameterizedTypeReference<>() {
        };
    }

    @Override
    protected LegalAssistance sampleOfferRequest() {
        LegalAssistance legalAssistance = new LegalAssistance();
        legalAssistance.title = "Help in legal issues";
        legalAssistance.description = "I can help";
        legalAssistance.location = new Location("lubelskie", "Lublin");

        return legalAssistance;
    }
}
