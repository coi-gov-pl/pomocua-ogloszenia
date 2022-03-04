package pl.gov.coi.pomocua.ads.jobs;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.PageableResponse;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JobsResourceTest extends BaseResourceTest<JobOffer> {

    @Override
    protected Class<JobOffer> getClazz() {
        return JobOffer.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "jobs";
    }


    @Override
    protected ParameterizedTypeReference<PageableResponse<JobOffer>> getResponseType() {
        return new ParameterizedTypeReference<>() {
        };
    }

    @Override
    protected JobOffer sampleOfferRequest() {
        JobOffer request = new JobOffer();
        request.title = "sample work";
        request.mode = JobOffer.Mode.REMOTE;
        request.city = "Warszawa";
        request.voivodeship = "Mazowieckie";
        request.type = List.of(JobOffer.Type.TEMPORARY);
        request.language = List.of(JobOffer.Language.PL, JobOffer.Language.UA);
        request.description = "description";
        return request;
    }
}