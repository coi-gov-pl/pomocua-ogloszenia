package pl.gov.coi.pomocua.ads.materialaid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaterialAidOfferManager {

    private final MaterialAidOfferRepository repository;

    public MaterialAidOffer saveMaterialAidOffer(final MaterialAidOffer materialAidOffer) {
        return repository.save(materialAidOffer);
    }
}
