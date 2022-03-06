package pl.gov.coi.pomocua.ads.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import pl.gov.coi.pomocua.ads.BaseResourceTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TranslationResourceTest extends BaseResourceTest<TranslationOffer> {
    @Autowired
    private TranslationOfferRepository repository;

    @Override
    protected String getOfferSuffix() {
        return "translations";
    }

    @Override
    protected Class<TranslationOffer> getClazz() {
        return TranslationOffer.class;
    }

    @Override
    protected TranslationOffer sampleOfferRequest() {
        TranslationOffer request = new TranslationOffer();
        request.title = "sample translation";
        request.mode = TranslationOffer.Mode.REMOTE;
        request.city = "Warszawa";
        request.region = "Mazowieckie";
        request.sworn = true;
        request.language = List.of(TranslationOffer.Language.PL, TranslationOffer.Language.UA);
        request.description = "description";
        return request;
    }

    @Override
    protected CrudRepository<TranslationOffer, Long> getRepository() {
        return repository;
    }
}