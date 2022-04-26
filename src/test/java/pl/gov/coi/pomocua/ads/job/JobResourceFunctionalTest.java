package pl.gov.coi.pomocua.ads.job;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import pl.gov.coi.pomocua.ads.BaseResourceFunctionalTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.job.JobOffer.Mode;
import pl.gov.coi.pomocua.ads.job.JobOffer.Industry;
import pl.gov.coi.pomocua.ads.job.JobOffer.WorkTime;
import pl.gov.coi.pomocua.ads.job.JobOffer.ContractType;

public class JobResourceFunctionalTest extends BaseResourceFunctionalTest {

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        jobOfferRepository.deleteAll();
    }

    @Override
    protected String getUrl() {
        return "/api/job";
    }

    @Override
    protected String postUrl() {
        return "/api/secure/job";
    }

    @Override
    protected String getBody() {
        return """
        {
          "title": "testTitle",
          "description": "testDescription",
          "location": {
            "region": "Mazowieckie",
            "city": "Warszawa"
          },
          "mode": "ONSITE",
          "industry": "FINANCES",
          "workTime": [
            "FULL_TIME",
            "PART_TIME"
          ],
          "contractType": [
            "EMPLOYMENT",
            "B2B"
          ],
          "language": [
            "UA",
            "PL"
          ],
          "phoneNumber": "+48123456789"
        }
        """;
    }

    @Override
    protected void assertResponseBody(ValidatableResponse response) {
        response
                .body("title", equalTo("testTitle"))
                .body("description", equalTo("testDescription"))
                .body("location.region", equalTo("Mazowieckie"))
                .body("location.city", equalTo("Warszawa"))
                .body("mode", equalTo("ONSITE"))
                .body("industry", equalTo("FINANCES"))
                .body("workTime", equalTo(List.of("FULL_TIME", "PART_TIME")))
                .body("contractType", equalTo(List.of("EMPLOYMENT", "B2B")))
                .body("language", equalTo(List.of("UA", "PL")));
    }

    @Override
    protected void assertFirstElement(ValidatableResponse response) {
        response
                .body("content[0].title", equalTo("testTitle"))
                .body("content[0].description", equalTo("testDescription"))
                .body("content[0].location.region", equalTo("Mazowieckie"))
                .body("content[0].location.city", equalTo("Warszawa"))
                .body("content[0].mode", equalTo("ONSITE"))
                .body("content[0].industry", equalTo("FINANCES"))
                .body("content[0].workTime", equalTo(List.of("FULL_TIME", "PART_TIME")))
                .body("content[0].contractType", equalTo(List.of("EMPLOYMENT", "B2B")))
                .body("content[0].language", equalTo(List.of("UA", "PL")));
    }

    @Override
    protected void assertOfferIsSavedInDb(Long id) {
        Optional<JobOffer> jobOfferOptional = jobOfferRepository.findById(id);
        assertThat(jobOfferOptional.isPresent()).isTrue();
        JobOffer jobOffer = jobOfferOptional.get();
        assertThat(jobOffer.title).isEqualTo("testTitle");
        assertThat(jobOffer.description).isEqualTo("testDescription");
        assertThat(jobOffer.location.region).isEqualTo("Mazowieckie");
        assertThat(jobOffer.location.city).isEqualTo("Warszawa");
        assertThat(jobOffer.mode).isEqualTo(Mode.ONSITE);
        assertThat(jobOffer.industry).isEqualTo(Industry.FINANCES);
        assertThat(jobOffer.getWorkTime()).isEqualTo(List.of(WorkTime.FULL_TIME, WorkTime.PART_TIME));
        assertThat(jobOffer.getContractType()).isEqualTo(List.of(ContractType.EMPLOYMENT, ContractType.B2B));
        assertThat(jobOffer.getLanguage()).isEqualTo(List.of(Language.UA, Language.PL));
    }
}
