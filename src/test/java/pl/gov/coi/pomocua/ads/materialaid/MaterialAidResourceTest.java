package pl.gov.coi.pomocua.ads.materialaid;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.PageableResponse;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MaterialAidResourceTest extends BaseResourceTest<MaterialAidOffer> {
    @Override
    protected Class<MaterialAidOffer> getClazz() {
        return MaterialAidOffer.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "material-aid";
    }

    @Override
    protected ParameterizedTypeReference<PageableResponse<MaterialAidOffer>> getResponseType() {
        return new ParameterizedTypeReference<>() {
        };
    }

    @Override
    protected MaterialAidOffer sampleOfferRequest() {
        MaterialAidOffer request = new MaterialAidOffer();
        request.description = "some description";
        request.title = "some title";
        request.category = MaterialAidCategory.CLOTHING;
        request.modifiedDate = LocalDateTime.parse("2020-10-17T00:00");
        return request;
    }
}
