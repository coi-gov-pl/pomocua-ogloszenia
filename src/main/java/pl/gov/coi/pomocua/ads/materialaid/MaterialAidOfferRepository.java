package pl.gov.coi.pomocua.ads.materialaid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface MaterialAidOfferRepository extends JpaRepository<MaterialAidOffer, BigInteger> {
}
