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

import static org.assertj.core.api.Assertions.assertThat;
import static pl.gov.coi.pomocua.ads.job.JobTestDataGenerator.aJobOffer;

import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.OffersVM;
import pl.gov.coi.pomocua.ads.job.JobOffer.Mode;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import pl.gov.coi.pomocua.ads.job.JobOffer.Industry;
import pl.gov.coi.pomocua.ads.job.JobOffer.WorkTime;
import pl.gov.coi.pomocua.ads.job.JobOffer.ContractType;

public class JobOfferResourceTest extends BaseResourceTest<JobOffer, JobOfferVM> {

    @Autowired
    private JobOfferRepository repository;

    @Override
    protected Class<JobOfferVM> getClazz() {
        return JobOfferVM.class;
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
    protected JobOfferVM sampleOfferRequest() {
        return aJobOffer().build();
    }

    @Nested
    class Searching {
        @Test
        void shouldSearchByLocationWithIgnoreCase() {
            JobOfferVM offer1 = postOffer(aJobOffer()
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
        void shouldSearchByLocationWithNullLocation() {
            JobOfferVM offer1 = postOffer(aJobOffer()
                    .location(null)
                    .build());
            JobOfferVM offer2 = postOffer(aJobOffer()
                    .location(new Location("Mazowieckie", "Warszawa"))
                    .build());
            postOffer(aJobOffer()
                    .location(new Location("Pomorskie", "Gdańsk"))
                    .build());

            JobOfferSearchCriteria searchCriteria = new JobOfferSearchCriteria();
            searchCriteria.setLocation(new Location("Mazowieckie", "WARSZAWA"));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer1, offer2);
        }

        @Test
        void shouldSearchByIndustry() {
            JobOfferVM offer = postOffer(aJobOffer()
                    .industry(Industry.SALES)
                    .build());

            JobOfferVM offer2 = postOffer(aJobOffer()
                    .industry(Industry.SALES)
                    .build());

            postOffer(aJobOffer()
                    .industry(Industry.CONSULTING)
                    .build());

            JobOfferSearchCriteria searchCriteria = new JobOfferSearchCriteria();
            searchCriteria.setIndustry(Industry.SALES);
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer, offer2);
        }

        @Test
        void shouldSearchByWorkTime() {
            JobOfferVM offer = postOffer(aJobOffer()
                    .workTime(List.of(WorkTime.FULL_TIME, WorkTime.TEMPORARY))
                    .build());

            JobOfferVM offer2 = postOffer(aJobOffer()
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
            JobOfferVM offer = postOffer(aJobOffer()
                    .contractType(List.of(ContractType.EMPLOYMENT))
                    .build());

            JobOfferVM offer2 = postOffer(aJobOffer()
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

            JobOfferVM offer = postOffer(aJobOffer()
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
            JobOfferVM offer = postOffer(aJobOffer()
                    .language(List.of(Language.PL, Language.UA))
                    .build());

            JobOfferVM offer2 = postOffer(aJobOffer()
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
            assertThat(results.content).hasSize(2).extracting(r -> r.getTitle()).containsExactly("c", "d");
        }

        @Test
        void shouldIgnoreDeactivatedOffer() {
            JobOfferVM offer = postOffer(aJobOffer().build());
            deleteOffer(offer.getId());

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
            JobOfferVM offer = sampleOfferRequest();
            offer.setLocation(null);
            assertPostResponseStatus(offer, HttpStatus.CREATED);
        }

        @Test
        void shouldRejectNullMode() {
            JobOfferVM offer = sampleOfferRequest();
            offer.setMode(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullIndustry() {
            JobOfferVM offer = sampleOfferRequest();
            offer.setIndustry(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullWorkTime() {
            JobOfferVM offer = sampleOfferRequest();
            offer.setWorkTime(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyWorkTime() {
            JobOfferVM offer = sampleOfferRequest();
            offer.setWorkTime(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullContractType() {
            JobOfferVM offer = sampleOfferRequest();
            offer.setContractType(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyContractType() {
            JobOfferVM offer = sampleOfferRequest();
            offer.setContractType(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullLanguage() {
            JobOfferVM offer = sampleOfferRequest();
            offer.setLanguage(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyLanguage() {
            JobOfferVM offer = sampleOfferRequest();
            offer.setLanguage(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class Updating {
        @Test
        void shouldUpdateOffer() {
            JobOfferVM offer = postSampleOffer();
            var updateJson = JobTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            JobOffer updatedOffer = getOfferFromRepository(offer.getId());
            assertThat(updatedOffer.title).isEqualTo("new title");
            assertThat(updatedOffer.description).isEqualTo("new description");
            assertThat(updatedOffer.mode).isEqualTo(Mode.TELEWORK);
            assertThat(updatedOffer.location.region).isEqualTo("Pomorskie");
            assertThat(updatedOffer.location.city).isEqualTo("Gdańsk");
            assertThat(updatedOffer.industry).isEqualTo(Industry.CONSULTING);
            assertThat(updatedOffer.getWorkTime()).isEqualTo(List.of(WorkTime.FULL_TIME, WorkTime.PART_TIME));
            assertThat(updatedOffer.getContractType()).isEqualTo(List.of(ContractType.B2B));
            assertThat(updatedOffer.getLanguage()).isEqualTo(List.of(Language.PL, Language.EN));
        }
    }

    private List<JobOfferVM> searchOffers(JobOfferSearchCriteria searchCriteria) {
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

    private OffersVM<JobOfferVM> searchOffers(PageRequest pageRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        builder.queryParam("page", pageRequest.getPageNumber());
        builder.queryParam("size", pageRequest.getPageSize());
        pageRequest.getSort().forEach(sort ->
                builder.queryParam("sort", "%s,%s".formatted(sort.getProperty(), sort.getDirection())));
        String url = builder.encode().toUriString();
        return listOffers(URI.create(url));
    }
}
