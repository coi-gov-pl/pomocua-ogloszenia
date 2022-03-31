package pl.gov.coi.pomocua.ads.myoffers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;

public interface MyOffersRepository extends PagingAndSortingRepository<BaseOffer, Long> {

    Page<BaseOffer> findAllByUserIdAndStatus(UserId userId, BaseOffer.Status status, Pageable pageable);

    Optional<BaseOffer> findByIdAndUserId(Long id, UserId userId);

    @Modifying
    @Query("UPDATE BaseOffer SET status = 'INACTIVE' WHERE userId = :userId")
    void setUserOffersAsInactive(@Param("userId") UserId userId);
}