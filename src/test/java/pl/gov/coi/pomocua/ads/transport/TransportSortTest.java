package pl.gov.coi.pomocua.ads.transport;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TransportSortTest {

    @Test
    void modifySort_whenPageRequestWithoutSortAndSearchCriteriaIsEmpty_expectEmptySort() {

        //given
        PageRequest pageRequest = PageRequest.of(1, 5);
        TransportOfferSearchCriteria searchCriteria = new TransportOfferSearchCriteria();

        //when
        Pageable pageable = TransportSort.modifySort(pageRequest, searchCriteria);

        //then
        Sort sort = pageable.getSort();
        assertThat(sort).isEmpty();
    }

    @Test
    void modifySort_whenPageRequestHasSortAndSearchCriteriaIsNotEmpty_expectSortIsPresent() {

        //given
        PageRequest pageRequest = PageRequest.of(1, 5, Sort.by("someProperty"));
        TransportOfferSearchCriteria searchCriteria = new TransportOfferSearchCriteria();
        searchCriteria.setTransportDate(LocalDate.now());

        //when
        Pageable pageable = TransportSort.modifySort(pageRequest, searchCriteria);

        //then
        Sort sort = pageable.getSort();
        assertThat(sort).hasSize(2);
        assertThat(sort).element(0).extracting(Sort.Order::getProperty).isEqualTo("transportDate");
        assertThat(sort).element(1).extracting(Sort.Order::getProperty).isEqualTo("someProperty");
    }
}