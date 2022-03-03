package pl.gov.coi.pomocua.ogloszenia.work;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.gov.coi.pomocua.ogloszenia.PageableResponse;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WorkResourceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateWorkOffer() {
        ResponseEntity<WorkOffer> response = restTemplate.postForEntity("/api/secure/offer/work", sampleWorkOfferRequest(), WorkOffer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldReturnCreatedWorkOfferOnList() {
        WorkOffer response = postSampleWorkOffer();

        ResponseEntity<PageableResponse<WorkOffer>> list = restTemplate.exchange("/api/offer/work", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        WorkOffer[] content = list.getBody().content;
        assertThat(content).contains(response);
    }

    @Test
    void shouldReturnCreatedWorkOffer() {
        WorkOffer created = postSampleWorkOffer();
        WorkOffer returned = restTemplate.getForObject("/api/offer/work/{id}", WorkOffer.class, created.id);
        assertThat(returned).isEqualTo(created);
    }

    @Test
    void shouldIgnoreSuppliedIdOnCreate() {
        WorkOffer request = sampleWorkOfferRequest();
        request.id = UUID.randomUUID();
        WorkOffer response = restTemplate.postForObject("/api/secure/offer/work", request, WorkOffer.class);
        assertThat(response.id).isNotNull();
        assertThat(response.id).isNotEqualTo(request.id);
    }

    private WorkOffer postSampleWorkOffer() {
        WorkOffer request = sampleWorkOfferRequest();
        WorkOffer response = restTemplate.postForObject("/api/secure/offer/work", request, WorkOffer.class);
        assertThat(response.id).isNotNull();
        assertThat(response).usingRecursiveComparison().ignoringFields("id").isEqualTo(request);
        return response;
    }

    private WorkOffer sampleWorkOfferRequest() {
        WorkOffer request = new WorkOffer();
        request.title = "sample work";
        request.mode = WorkOffer.Mode.REMOTE;
        request.city = "Warszawa";
        request.voivodeship = "Mazowieckie";
        request.type = List.of(WorkOffer.Type.TEMPORARY);
        request.language = List.of(WorkOffer.Language.PL, WorkOffer.Language.UA);
        request.description = "description";
        return request;
    }
}