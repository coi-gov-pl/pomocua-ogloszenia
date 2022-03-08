package pl.gov.coi.pomocua.ads;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

public class TestTimeProvider extends TimeProvider {
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public void reset() {
        this.clock = Clock.systemDefaultZone();
    }

    public void setCurrentTime(Instant time) {
        setClock(Clock.fixed(time, ZoneOffset.UTC));
    }
}
