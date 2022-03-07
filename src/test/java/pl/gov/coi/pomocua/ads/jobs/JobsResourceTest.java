package pl.gov.coi.pomocua.ads.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import pl.gov.coi.pomocua.ads.BaseResourceTest;

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
    protected JobOffer sampleOfferRequest() {
        return JobsTestDataGenerator.sampleOffer();
    }

    @Override
    protected CrudRepository<JobOffer, Long> getRepository() {
        return repository;
    }
}