package pl.gov.coi.pomocua.ads.materialaid.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.gov.coi.pomocua.ads.materialaid.domain.entity.MaterialAidOfferEntity;

import java.math.BigInteger;

@Repository
public interface MaterialAidOfferRepository extends JpaRepository<MaterialAidOfferEntity, BigInteger> {
}
