package pl.gov.coi.pomocua.ads;

import java.time.Clock;

public class TestTimeProvider extends TimeProvider {
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public void reset() {
        this.clock = Clock.systemDefaultZone();
    }
}
