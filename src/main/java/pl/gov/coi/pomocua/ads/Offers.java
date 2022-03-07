package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor(onConstructor_ = @JsonCreator, access = AccessLevel.PRIVATE)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public class Offers<T extends BaseOffer> {
    @JsonBaseOfferInheritance
    public final List<T> content;
    public final long totalElements;
    public final int totalPages;

    private Offers(Page<T> p) {
        this.content = p.getContent();
        this.totalElements = p.getTotalElements();
        this.totalPages = p.getTotalPages();
    }

    public static <T extends BaseOffer> Offers<T> page(Page<T> page) {
        return new Offers<>(page);
    }
}
