package at.connectTUdoc.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This class contains the configuration for the util swagger.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final String applicationVersion;

    public SwaggerConfig(@Value("${spring.application.version}") String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }


    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("at.ws18_ase_qse_03.backend"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("MedicalConnect")
                .description("MedicalConnect API reference for developers")
                .termsOfServiceUrl("https://reset.inso.tuwien.ac.at/")
                .license("TU-Vienna License")
                .licenseUrl("medconnect@gmail.com")
                .version(applicationVersion)
                .build();
    }

}
