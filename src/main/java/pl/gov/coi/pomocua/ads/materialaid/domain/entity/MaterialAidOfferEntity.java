package pl.gov.coi.pomocua.ads.materialaid.domain.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.gov.coi.pomocua.ads.dictionaries.domain.City;
import pl.gov.coi.pomocua.ads.materialaid.domain.dto.MaterialAidOfferDto;
import pl.gov.coi.pomocua.ads.materialaid.domain.dto.MaterialAidCategory;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.EnumType.STRING;

@Entity
@Data
@NoArgsConstructor
@Table(name = "MATERIAL_AID_OFFER")
public class MaterialAidOfferEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    protected Long id;

    @Getter
    private LocalDateTime creationDate;

    @Enumerated(STRING)
    private MaterialAidCategory category;

    @OneToOne
    private City location;

    private String title;
    private String content;

    public MaterialAidOfferEntity(MaterialAidOfferDto dto) {
        category = dto.getCategory();
        location = dto.getLocation();
        title = dto.getTitle();
        content = dto.getContent();
    }
}
