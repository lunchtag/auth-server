package nl.lunchtag.auth.LunchtagAuth.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Controller
@Configuration
@ApiIgnore
public class SwaggerConfiguration {

    @GetMapping("/api")
    public String swaggerHtml() {
        return "redirect:/swagger-ui.html";
    }

    @Bean
    public Docket API() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("LunchTagAPI")
                        .description("")
                        .build()
                ).tags(
                        new Tag("Auth", "Endpoints for authentication"),
                        new Tag("User", "Endpoints for user")
                );
    }
}