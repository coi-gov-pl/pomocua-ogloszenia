package pl.gov.coi.pomocua.ads.materialaid;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.PageableResponse;
import pl.gov.coi.pomocua.ads.UserId;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MaterialAidTest extends BaseResourceTest<MaterialAidOffer> {

    @Override
    protected Class<MaterialAidOffer> getClazz() { return MaterialAidOffer.class; }

    @Override
    protected String getOfferSuffix() { return "material-aid"; }


    @Override
    protected ParameterizedTypeReference<PageableResponse<MaterialAidOffer>> getResponseType() {
        return new ParameterizedTypeReference<>(){
        };
    }

    @Override
    protected MaterialAidOffer sampleOfferRequest() {
        MaterialAidOffer request = new MaterialAidOffer();
        request.title = "sample aid";
        request.description = "sample desc";
        request.userId = new UserId("21");
        request.category = MaterialAidCategory.CLOTHING;
        request.location = new Location("Małopolskie", "Wadowice");
        return request;
    }
}
