package pl.gov.coi.pomocua.ads.job;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.gov.coi.pomocua.ads.job.JobTestDataGenerator.aJobOffer;

import pl.gov.coi.pomocua.ads.Offers;
import pl.gov.coi.pomocua.ads.job.JobOffer.Mode;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import pl.gov.coi.pomocua.ads.job.JobOffer.Industry;
import pl.gov.coi.pomocua.ads.job.JobOffer.WorkTime;
import pl.gov.coi.pomocua.ads.job.JobOffer.ContractType;
import pl.gov.coi.pomocua.ads.job.JobOffer.Mode;

public class JobOfferResourceTest extends BaseResourceTest<JobOffer> {

    @Autowired
    private JobOfferRepository repository;


    @Override
    protected Class<JobOffer> getClazz() {
        return JobOffer.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "job";
    }

    @Override
    protected CrudRepository<JobOffer, Long> getRepository() {
        return repository;
    }

    @Override
    protected JobOffer sampleOfferRequest() {
        return aJobOffer().build();
    }

    @Nested
    class Searching {
        @Test
        void shouldSearchByLocationWithIgnoreCase() {
            JobOffer offer1 = postOffer(aJobOffer()
                    .location(new Location("Mazowieckie", "Warszawa"))
                    .build());

            postOffer(aJobOffer()
                    .location(new Location("Pomorskie", "Gdańsk"))
                    .build());

            JobOfferSearchCriteria searchCriteria = new JobOfferSearchCriteria();
            searchCriteria.setLocation(new Location("Mazowieckie", "WARSZAWA"));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(1).contains(offer1);
        }

        @Test
        void shouldSearchByIndustry() {
            JobOffer offer = postOffer(aJobOffer()
                    .industry(List.of(Industry.TRADE))
                    .build());

            JobOffer offer2 = postOffer(aJobOffer()
                    .industry(List.of(Industry.TRADE, Industry.CONSULTING))
                    .build());

            postOffer(aJobOffer()
                    .industry(List.of(Industry.CONSULTING, Industry.DESK_JOB))
                    .build());

            JobOfferSearchCriteria searchCriteria = new JobOfferSearchCriteria();
            searchCriteria.setIndustry(Industry.TRADE);
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer, offer2);
        }

        @Test
        void shouldSearchByWorkTime() {
            JobOffer offer = postOffer(aJobOffer()
                    .workTime(List.of(WorkTime.FULL_TIME, WorkTime.TEMPORARY))
                    .build());

            JobOffer offer2 = postOffer(aJobOffer()
                    .workTime(List.of(WorkTime.TEMPORARY))
                    .build());

            postOffer(aJobOffer()
                    .workTime(List.of(WorkTime.FULL_TIME, WorkTime.PART_TIME))
                    .build());

            JobOfferSearchCriteria searchCriteria = new JobOfferSearchCriteria();
            searchCriteria.setWorkTime(WorkTime.TEMPORARY);
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer, offer2);
        }

        @Test
        void shouldSearchByContractType() {
            JobOffer offer = postOffer(aJobOffer()
                    .contractType(List.of(ContractType.EMPLOYMENT))
                    .build());

            JobOffer offer2 = postOffer(aJobOffer()
                    .contractType(List.of(ContractType.B2B, ContractType.EMPLOYMENT))
                    .build());

            postOffer(aJobOffer()
                    .contractType(List.of(ContractType.SPECIFIC_TASK))
                    .build());

            JobOfferSearchCriteria searchCriteria = new JobOfferSearchCriteria();
            searchCriteria.setContractType(ContractType.EMPLOYMENT);
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer, offer2);
        }

        @Test
        void shouldSearchByMode() {
            postOffer(aJobOffer()
                    .mode(Mode.ONSITE)
                    .build());

            JobOffer offer = postOffer(aJobOffer()
                    .mode(Mode.TELEWORK)
                    .build());

            postOffer(aJobOffer()
                    .mode(Mode.MIXED)
                    .build());

            JobOfferSearchCriteria searchCriteria = new JobOfferSearchCriteria();
            searchCriteria.setMode(Mode.TELEWORK);
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(1).contains(offer);
        }

        @Test
        void shouldSearchByLanguage() {
            JobOffer offer = postOffer(aJobOffer()
                    .language(List.of(Language.PL, Language.UA))
                    .build());

            JobOffer offer2 = postOffer(aJobOffer()
                    .language(List.of(Language.EN))
                    .build());

            postOffer(aJobOffer()
                    .language(List.of(Language.UA, Language.RU))
                    .build());

            JobOfferSearchCriteria searchCriteria = new JobOfferSearchCriteria();
            searchCriteria.setLanguage(List.of(Language.PL, Language.EN));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer, offer2);
        }

        @Test
        void shouldReturnPageOfData() {
            postOffer(aJobOffer().title("a").build());
            postOffer(aJobOffer().title("b").build());
            postOffer(aJobOffer().title("c").build());
            postOffer(aJobOffer().title("d").build());
            postOffer(aJobOffer().title("e").build());
            postOffer(aJobOffer().title("f").build());

            PageRequest page = PageRequest.of(1, 2);
            var results = searchOffers(page);

            assertThat(results.totalElements).isEqualTo(6);
            assertThat(results.content).hasSize(2).extracting(r -> r.title).containsExactly("c", "d");
        }

        @Test
        void shouldIgnoreDeactivatedOffer() {
            JobOffer offer = postOffer(aJobOffer().build());
            deleteOffer(offer.id);

            PageRequest page = PageRequest.of(0, 10);
            var results = searchOffers(page);

            assertThat(results.totalElements).isEqualTo(0);
            assertThat(results.content).isEmpty();
        }
    }

    @Nested
    class ValidationTest {
        @Test
        void shouldAcceptNullLocation() {
            JobOffer offer = sampleOfferRequest();
            offer.location = null;
            assertPostResponseStatus(offer, HttpStatus.CREATED);
        }

        @Test
        void shouldRejectNullMode() {
            JobOffer offer = sampleOfferRequest();
            offer.mode = null;
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullIndustry() {
            JobOffer offer = sampleOfferRequest();
            offer.setIndustry(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyIndustry() {
            JobOffer offer = sampleOfferRequest();
            offer.setIndustry(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullWorkTime() {
            JobOffer offer = sampleOfferRequest();
            offer.setWorkTime(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyWorkTime() {
            JobOffer offer = sampleOfferRequest();
            offer.setWorkTime(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullContractType() {
            JobOffer offer = sampleOfferRequest();
            offer.setContractType(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyContractType() {
            JobOffer offer = sampleOfferRequest();
            offer.setContractType(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullLanguage() {
            JobOffer offer = sampleOfferRequest();
            offer.setLanguage(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyLanguage() {
            JobOffer offer = sampleOfferRequest();
            offer.setLanguage(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class Updating {
        @Test
        void shouldUpdateOffer() {
            JobOffer offer = postSampleOffer();
            var updateJson = JobTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            JobOffer updatedOffer = getOfferFromRepository(offer.id);
            assertThat(updatedOffer.title).isEqualTo("new title");
            assertThat(updatedOffer.description).isEqualTo("new description");
            assertThat(updatedOffer.mode).isEqualTo(Mode.TELEWORK);
            assertThat(updatedOffer.location.region).isEqualTo("Pomorskie");
            assertThat(updatedOffer.location.city).isEqualTo("Gdańsk");
            assertThat(updatedOffer.getIndustry()).isEqualTo(List.of(Industry.CONSULTING));
            assertThat(updatedOffer.getWorkTime()).isEqualTo(List.of(WorkTime.FULL_TIME, WorkTime.PART_TIME));
            assertThat(updatedOffer.getContractType()).isEqualTo(List.of(ContractType.B2B));
            assertThat(updatedOffer.getLanguage()).isEqualTo(List.of(Language.PL, Language.EN));
        }
    }

    private List<JobOffer> searchOffers(JobOfferSearchCriteria searchCriteria) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        if (searchCriteria.getLocation() != null) {
            builder
                    .queryParam("location.city", searchCriteria.getLocation().getCity())
                    .queryParam("location.region", searchCriteria.getLocation().getRegion());
        }
        if (searchCriteria.getIndustry() != null) {
            builder.queryParam("industry", searchCriteria.getIndustry());
        }
        if (searchCriteria.getWorkTime() != null) {
            builder.queryParam("workTime", searchCriteria.getWorkTime());
        }
        if (searchCriteria.getContractType() != null) {
            builder.queryParam("contractType", searchCriteria.getContractType());
        }
        if (searchCriteria.getMode() != null) {
            builder.queryParam("mode", searchCriteria.getMode());
        }
        if (!CollectionUtils.isEmpty(searchCriteria.getLanguage())) {
            builder.queryParam("language", searchCriteria.getLanguage());
        }
        String url = builder.encode().toUriString();
        return listOffers(URI.create(url)).content;
    }

    private Offers<JobOffer> searchOffers(PageRequest pageRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        builder.queryParam("page", pageRequest.getPageNumber());
        builder.queryParam("size", pageRequest.getPageSize());
        pageRequest.getSort().forEach(sort ->
                builder.queryParam("sort", "%s,%s".formatted(sort.getProperty(), sort.getDirection())));
        String url = builder.encode().toUriString();
        return listOffers(URI.create(url));
    }
}
