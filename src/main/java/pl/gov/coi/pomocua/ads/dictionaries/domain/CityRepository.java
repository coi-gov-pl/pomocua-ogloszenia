package pl.gov.coi.pomocua.ads.dictionaries.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityRepository extends CrudRepository<City, Long> {

    List<City> findByVoivodeshipAndCityStartsWith(String voivodeship, String city);
}
