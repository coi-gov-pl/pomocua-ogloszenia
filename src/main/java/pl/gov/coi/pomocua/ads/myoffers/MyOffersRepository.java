package pl.gov.coi.pomocua.ads.myoffers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;

public interface MyOffersRepository extends PagingAndSortingRepository<BaseOffer, Long> {

    Page<BaseOffer> findAllByUserIdAndStatus(UserId userId, BaseOffer.Status status, Pageable pageable);

    Optional<BaseOffer> findByIdAndUserId(Long id, UserId userId);
}