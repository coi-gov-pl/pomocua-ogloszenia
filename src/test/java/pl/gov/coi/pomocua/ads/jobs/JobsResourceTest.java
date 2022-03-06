package pl.gov.coi.pomocua.ads.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.repository.CrudRepository;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.PageableResponse;

import java.util.List;

class JobsResourceTest extends BaseResourceTest<JobOffer> {

    @Autowired
    private JobsRepository repository;

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
        request.location = new Location("Mazowieckie", "Warszawa");
        request.type = List.of(JobOffer.Type.TEMPORARY);
        request.language = List.of(JobOffer.Language.PL, JobOffer.Language.UA);
        request.description = "description";
        return request;
    }

    @Override
    protected CrudRepository<JobOffer, Long> getRepository() {
        return repository;
    }
}