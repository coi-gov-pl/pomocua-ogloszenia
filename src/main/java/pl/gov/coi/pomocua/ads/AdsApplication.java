package pl.gov.coi.pomocua.ads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
public class AdsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdsApplication.class, args);
	}
}
