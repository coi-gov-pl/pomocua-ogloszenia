import io.gatling.javaapi.core.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class AddTransportResourceSimulationSimulation extends Simulation {

  private static final String URL = "/api/secure/transport";

  private static final FeederBuilder feeder = csv("cities.csv").random();

  private static final String BODY = """
        {
          "title": "testTitle",
          "description": "testDescription",
          "origin": {
            "region": "${originCity}",
            "city": "Kraków"
          },
          "destination": {
            "region": "Mazowieckie",
            "city": "Warszawa"
          },
          "capacity": 3,
          "transportDate": "2022-03-05"
        }
      """;

  ScenarioBuilder scn = scenario("Add transport resource simulation")
      .feed(feeder)
      .exec(http("add transport resource request")
          .post(URL)
          .body(StringBody(BODY))
          .asJson()
      );

  {
    setUp(
        scn.injectOpen(constantUsersPerSec(100).during(Duration.ofMinutes(1)))
    ).protocols(Commons.httpProtocol);
  }
}
