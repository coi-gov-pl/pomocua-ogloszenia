package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor(onConstructor_ = @JsonCreator, access = AccessLevel.PRIVATE)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public class OffersVM<T extends BaseOfferVM> {
    @JsonBaseOfferVMInheritance
    public final List<T> content;
    public final long totalElements;
    public final int totalPages;

    private OffersVM(Page<T> p) {
        this.content = p.getContent();
        this.totalElements = p.getTotalElements();
        this.totalPages = p.getTotalPages();
    }

    public static <T extends BaseOfferVM> OffersVM<T> page(Page<T> page) { return new OffersVM<>(page); }
}
