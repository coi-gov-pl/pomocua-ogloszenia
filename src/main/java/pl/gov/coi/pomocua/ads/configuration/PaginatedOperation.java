package pl.gov.coi.pomocua.ads.configuration;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Parameters({
        @Parameter(name = "page", in = ParameterIn.QUERY, description = "0-based page number"),
        @Parameter(name = "size", in = ParameterIn.QUERY, description = "size of a page"),
        @Parameter(name = "sort", in = ParameterIn.QUERY, description = "allow to define sort field(s)", example = "?sort=title,desc")
})
@Retention(RetentionPolicy.RUNTIME)
public @interface PaginatedOperation {
}
