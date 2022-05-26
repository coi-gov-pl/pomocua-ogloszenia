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
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOfferVM;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonBaseOfferVMInheritanceTest {
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mapper = JsonMapper.builder()
                .configure(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL, true)
                .build();
    }

    @Test
    void one() throws JsonProcessingException {
        var offer = new MaterialAidOfferVM();
        offer.setTitle("title");
        var json = mapper.writeValueAsString(offer);
        var o = mapper.readValue(json, BaseOfferVM.class);
        assertThat(o).isEqualTo(offer);
    }

    @Test
    void list() throws JsonProcessingException {
        var offer = new MaterialAidOfferVM();
        offer.setTitle("title");
        Paged<MaterialAidOfferVM> initial = new Paged<>(List.of(offer));
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
        var offer = mapper.readValue(json, MaterialAidOfferVM.class);
        assertThat(offer.getTitle()).isEqualTo("title");
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    static class Paged<T> {
        @JsonBaseOfferVMInheritance
        public List<T> content;
    }
}
