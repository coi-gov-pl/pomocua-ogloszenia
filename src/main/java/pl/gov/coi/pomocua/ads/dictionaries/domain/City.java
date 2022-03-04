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
    private Location location;

    protected City() {
    }

    public City(String city, String region) {
        this.location = new Location(region, city);
    }

    public Location getLocation() {
        return location;
    }
}
