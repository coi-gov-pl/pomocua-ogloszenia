package pl.gov.coi.pomocua.ads.dictionaries.domain;

import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.*;

@Entity
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_gen")
    @SequenceGenerator(name="city_gen", sequenceName="city_seq")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "voivodeship", column = @Column(name = "voivodeship"))
    })
    private Location location;

    protected City() {
    }

    public City(String city, String voivodeship) {
        this.location = new Location(voivodeship, city);
    }

    public Location getLocation() {
        return location;
    }
}
