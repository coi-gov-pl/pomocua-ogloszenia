package pl.gov.coi.pomocua.ads;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Location {
    @NotBlank
    public String region;
    @NotBlank
    public String city;
}
