package pl.gov.coi.pomocua.ads;

import java.time.Clock;
import java.time.Instant;

public class TimeProvider {
    protected Clock clock = Clock.systemDefaultZone();

    public Instant now() {
        return Instant.now(clock);
    }
}
