package pl.gov.coi.pomocua.ads.transport;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.BaseOffer;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public class TransportOffer extends BaseOffer {

}
