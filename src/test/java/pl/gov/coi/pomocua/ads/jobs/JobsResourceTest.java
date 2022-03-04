package pl.gov.coi.pomocua.ads.jobs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.gov.coi.pomocua.ads.PageableResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JobsResourceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateWorkOffer() {
        assertPostResponseStatus(sampleWorkOfferRequest(), HttpStatus.CREATED);
    }

    @Test
    void shouldReturnCreatedWorkOfferOnList() {
        JobOffer response = postSampleWorkOffer();

        JobOffer[] content = listWorkOffer();
        assertThat(content).contains(response);
    }

    @Test
    void shouldReturnCreatedWorkOffer() {
        JobOffer created = postSampleWorkOffer();
        JobOffer returned = restTemplate.getForObject("/api/jobs/{id}", JobOffer.class, created.id);
        assertThat(returned).isEqualTo(created);
    }

    @Test
    void shouldRejectBlankTitle() {
        JobOffer workOffer = sampleWorkOfferRequest();
        workOffer.title = "       ";
        assertPostResponseStatus(workOffer, HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldRejectTooLongTitle() {
        JobOffer workOffer = sampleWorkOfferRequest();
        workOffer.title = "x".repeat(100);
        assertPostResponseStatus(workOffer, HttpStatus.BAD_REQUEST);
    }

    private void assertPostResponseStatus(JobOffer workOffer, HttpStatus badRequest) {
        ResponseEntity<JobOffer> response = restTemplate.postForEntity("/api/secure/jobs", workOffer, JobOffer.class);
        assertThat(response.getStatusCode()).isEqualTo(badRequest);
    }

    @Test
    void shouldIgnoreSuppliedIdOnCreate() {
        JobOffer request = sampleWorkOfferRequest();
        request.id = 42L;
        JobOffer response = restTemplate.postForObject("/api/secure/jobs", request, JobOffer.class);
        assertThat(response.id).isNotNull();
        assertThat(response.id).isNotEqualTo(request.id);
    }

    private JobOffer[] listWorkOffer() {
        var list = restTemplate.exchange("/api/jobs", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<JobOffer>>() {
                });
        return list.getBody().content;
    }

    private JobOffer postSampleWorkOffer() {
        JobOffer request = sampleWorkOfferRequest();
        JobOffer response = restTemplate.postForObject("/api/secure/jobs", request, JobOffer.class);
        assertThat(response.id).isNotNull();
        assertThat(response).usingRecursiveComparison().ignoringFields("id").isEqualTo(request);
        return response;
    }

    private JobOffer sampleWorkOfferRequest() {
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