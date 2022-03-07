package pl.gov.coi.pomocua.ads.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.lang.reflect.Method;
import java.util.List;

@Configuration
@Profile("api-docs")
public class ApiDocsConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info().title("Documentation for ads portal").version("1.0"))
                .servers(List.of(new Server().url("/")));
    }

    @Bean
    public OperationCustomizer customizer() {
        return (operation, handlerMethod) -> {
            Method method = handlerMethod.getMethod();
            String methodName = method.getName();
            String resource = method.getDeclaringClass().getSimpleName().replace("Resource", "");
            operation.setOperationId(methodName + resource);
            return operation;
        };
    }

}

