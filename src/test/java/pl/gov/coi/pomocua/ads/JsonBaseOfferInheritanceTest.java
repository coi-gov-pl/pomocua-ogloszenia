package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.gov.coi.pomocua.ads.jobs.JobOffer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonBaseOfferInheritanceTest {
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mapper = JsonMapper.builder()
                .configure(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL, true)
                .build();
    }

    @Test
    void one() throws Exception {
        var job = new JobOffer();
        job.title = "title";
        var json = mapper.writeValueAsString(job);
        var o = mapper.readValue(json, BaseOffer.class);
        assertThat(o).isEqualTo(job);
    }

    @Test
    void list() throws Exception {
        var job = new JobOffer();
        job.title = "title";
        Paged initial = new Paged(List.of(job));
        var json = mapper.writeValueAsString(initial);
        var o = mapper.readValue(json, Paged.class);
        assertThat(o).isEqualTo(initial);
    }

    @Test
    void deserialize() throws JsonProcessingException {
        var json = """
                    {
                      "id": null,
                      "title": "title",
                      "description": null,
                      "type": null,
                      "mode": null,
                      "location": null,
                      "language": null
                    }
                """;
        var job = mapper.readValue(json, JobOffer.class);
        assertThat(job.title).isEqualTo("title");
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    static class Paged<T> {
        @JsonBaseOfferInheritance
        public List<T> content;
    }
}
