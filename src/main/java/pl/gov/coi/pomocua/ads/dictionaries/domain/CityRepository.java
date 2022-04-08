package pl.gov.coi.pomocua.ads.dictionaries.domain;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends CrudRepository<City, Long> {

    @Query("SELECT c FROM City c WHERE UPPER(CONCAT(c.location.city, ', ', c.location.region)) LIKE UPPER(CONCAT(:query, '%'))")
    List<City> findByLocationCityWithSort(@Param("query") String query, Sort sort);
}
