package pl.gov.coi.pomocua.ads.materialaid;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.PageableResponse;
import pl.gov.coi.pomocua.ads.UserId;

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
        request.userId= new UserId("1");
        request.description = "some description";
        request.title = "some title";
        request.category = MaterialAidCategory.CLOTHING;
        return request;
    }
}
