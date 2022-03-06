package pl.gov.coi.pomocua.ads;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfiguration.class)
class AdsApplicationTests {

	@Test
	void contextLoads() {
	}

}
