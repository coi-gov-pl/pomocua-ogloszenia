package pl.gov.coi.pomocua.ads.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import pl.gov.coi.pomocua.ads.TimeProvider;

import java.util.Optional;

@Configuration
public class TimeConfiguration {
    @Bean
    public TimeProvider timeProvider() {
        return new TimeProvider();
    }

    @Bean
    public DateTimeProvider dateTimeProvider(TimeProvider timeProvider) {
        return () -> Optional.of(timeProvider.now());
    }
}
