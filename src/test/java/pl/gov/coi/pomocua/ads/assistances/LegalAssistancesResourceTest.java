package pl.gov.coi.pomocua.ads.assistances;

import org.springframework.core.ParameterizedTypeReference;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.PageableResponse;
import pl.gov.coi.pomocua.ads.assistance.LegalAssistanceOffer;

import java.util.List;

public class LegalAssistancesResourceTest extends BaseResourceTest<LegalAssistanceOffer> {

    @Override
    protected Class<LegalAssistanceOffer> getClazz() {
        return LegalAssistanceOffer.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "legal_assistances";
    }

    @Override
    protected ParameterizedTypeReference<PageableResponse<LegalAssistanceOffer>> getResponseType() {
        return new ParameterizedTypeReference<>() {
        };
    }

    @Override
    protected LegalAssistanceOffer sampleOfferRequest() {
        LegalAssistanceOffer legalAssistanceOffer = new LegalAssistanceOffer();
        legalAssistanceOffer.title = "Help in legal issues";
        legalAssistanceOffer.description = "I can help";
        legalAssistanceOffer.location = new Location("lubelskie", "Lublin");
        legalAssistanceOffer.type = List.of(LegalAssistanceOffer.Type.TEMPORARY);
        legalAssistanceOffer.mode = LegalAssistanceOffer.Mode.REMOTE;
        legalAssistanceOffer.language = List.of(LegalAssistanceOffer.Language.PL);
        return legalAssistanceOffer;
    }
}
