import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

public class Commons {

  private static final String BASE_URL = "http://localhost:8080";

  static HttpProtocolBuilder httpProtocol = http
      .baseUrl(BASE_URL);
}
