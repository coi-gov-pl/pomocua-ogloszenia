package pl.gov.coi.pomocua.ads;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Location {
    public static final String ALLOWED_TEXT = "^[\\p{IsAlphabetic} -.,()]*$";

    @NotBlank
    @Pattern(regexp = ALLOWED_TEXT)
    public String region;

    @Pattern(regexp = ALLOWED_TEXT)
    @NotBlank
    public String city;
}
