package pl.gov.coi.pomocua.ads.materialaid.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.gov.coi.pomocua.ads.dictionaries.domain.City;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Builder
public class MaterialAidAdDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 4899363368417701147L;
    private static final String EXCLUDING_SPECIAL_CHARS = "^[^<>()%#@”‘]*$";

    private MaterialAidCategory category;
    private City location;

    @Size(max = 255)
    @Pattern(regexp = EXCLUDING_SPECIAL_CHARS)
    private String title;
    @Size(max = 1024)
    @Pattern(regexp = EXCLUDING_SPECIAL_CHARS)
    private String content;
}
