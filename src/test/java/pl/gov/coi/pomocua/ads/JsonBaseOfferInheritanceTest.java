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
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOffer;

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
        var offer = new MaterialAidOffer();
        offer.title = "title";
        var json = mapper.writeValueAsString(offer);
        var o = mapper.readValue(json, BaseOffer.class);
        assertThat(o).isEqualTo(offer);
    }

    @Test
    void list() throws Exception {
        var offer = new MaterialAidOffer();
        offer.title = "title";
        Paged<MaterialAidOffer> initial = new Paged<>(List.of(offer));
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
                      "category": null,
                      "location": null
                    }
                """;
        var offer = mapper.readValue(json, MaterialAidOffer.class);
        assertThat(offer.title).isEqualTo("title");
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    static class Paged<T> {
        @JsonBaseOfferInheritance
        public List<T> content;
    }
}
