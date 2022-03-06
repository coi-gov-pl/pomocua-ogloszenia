import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class SearchTransportResourceSimulation extends Simulation {

  private static final String POST_URL = "/api/secure/transport";
  private static final String GET_URL = "/api/transport";

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

  ScenarioBuilder scn = scenario("Get transport resource simulation")
      .exec(
          feed(feeder).exec(http("add transport resource request")
              .post(POST_URL)
              .body(StringBody(BODY))
              .asJson())
      )
      .exec(http("get transport resource request")
          .get(GET_URL)
          .queryParam("origin.city", "besko")
          .asJson()
      );

  {
    setUp(
        scn.injectOpen(constantUsersPerSec(100).during(Duration.ofMinutes(1)))
    ).protocols(Commons.httpProtocol);
  }
}
