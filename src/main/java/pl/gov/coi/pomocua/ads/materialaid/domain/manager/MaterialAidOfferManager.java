package pl.gov.coi.pomocua.ads.materialaid.domain.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gov.coi.pomocua.ads.materialaid.domain.dto.MaterialAidOfferDto;
import pl.gov.coi.pomocua.ads.materialaid.domain.entity.MaterialAidOfferEntity;
import pl.gov.coi.pomocua.ads.materialaid.domain.repository.MaterialAidOfferRepository;

@Service
@RequiredArgsConstructor
public class MaterialAidOfferManager {

    private final MaterialAidOfferRepository repository;

    public MaterialAidOfferEntity saveMaterialAidOffer(final MaterialAidOfferDto dto) {
        return repository.save(new MaterialAidOfferEntity(dto));
    }
}
