package pl.gov.coi.pomocua.ads.dictionaries.domain;

import javax.persistence.*;

@Entity
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_gen")
    @SequenceGenerator(name="city_gen", sequenceName="city_seq")
    private Long id;

    private String voivodeship;
    private String city;

    protected City() {
    }

    public City(String city, String voivodeship) {
        this.city = city;
        this.voivodeship = voivodeship;
    }

    public String getCity() {
        return this.city;
    }

    public String getVoivodeship() {
        return this.voivodeship;
    }
}
