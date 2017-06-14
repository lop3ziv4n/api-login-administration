package ar.org.blb.login.administration.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metadata());
    }

    private ApiInfo metadata() {
        ApiInfo apiInfo = new ApiInfo(
                "Login Administration API",
                "Login and Register User",
                "0.1-SNAPSHOT",
                "Terms of service",
                new Contact("BLB Solution System", "https://blb-solution-system.org", "blb.solution.system@gmail.com"),
                "BLB License Version 2.0",
                "https://www.blb-solution-system.org/licenses/LICENSE-2.0");
        return apiInfo;
    }
}
