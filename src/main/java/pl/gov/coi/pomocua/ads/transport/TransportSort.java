package pl.gov.coi.pomocua.ads.transport;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class TransportSort {

    private static List<Sort.Order> getOrdersBySearchCriteria(TransportOfferSearchCriteria searchCriteria) {
        List<Sort.Order> orders = new ArrayList<>();

        if (searchCriteria.getOrigin() != null) {
            orders.add(Sort.Order.by("origin.city"));
        }
        if (searchCriteria.getDestination() != null) {
            orders.add(Sort.Order.by("destination.city"));
        }
        if (searchCriteria.getTransportDate() != null) {
            orders.add(Sort.Order.by("transportDate"));
        }

        return orders;
    }

    public static Pageable modifySort(PageRequest pageRequest, TransportOfferSearchCriteria searchCriteria) {

        List<Sort.Order> orders = getOrdersBySearchCriteria(searchCriteria);
        return pageRequest.withSort(Sort.by(orders).and(pageRequest.getSort()));
    }
}
