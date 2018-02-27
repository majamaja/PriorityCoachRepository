package com.futuristlabs.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket mobile() {
		return new Docket(DocumentationType.SWAGGER_2)
				.ignoredParameterTypes(AuthenticationPrincipal.class)
				.groupName("codehospitality-mobile-api")
				.directModelSubstitute(LocalDateTime.class, Date.class)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.codehospitality.rest.v1_0"))
				.paths(PathSelectors.any())
				.build()
				//according to bug https://github.com/springfox/springfox/issues/1569 ApiKey name should match reference in SecurityReference
				.securitySchemes(Collections.singletonList(new ApiKey("X-Auth-Token", "xAuthToken", "header")))
				.securityContexts(Collections.singletonList(generateXAuthTokenSecurityContext()))
				.apiInfo(new ApiInfo("Code Hospitality MOBILE API definitions", "Code Hospitality MOBILE API definitions", "1.0", null, (Contact) null, null, null, Collections.emptyList()));
	}

	@Bean
	public Docket admin() {
		return new Docket(DocumentationType.SWAGGER_2)
				.ignoredParameterTypes(AuthenticationPrincipal.class)
				.groupName("codehospitality-admin-api")
				.directModelSubstitute(LocalDateTime.class, Date.class)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.codehospitality.rest.admin"))
				.paths(PathSelectors.any())
				.build()
				//according to bug https://github.com/springfox/springfox/issues/1569 ApiKey name should match reference in SecurityReference
				.securitySchemes(Collections.singletonList(new ApiKey("X-Auth-Token", "xAuthToken", "header")))
				.securityContexts(Collections.singletonList(generateXAuthTokenSecurityContext()))
				.apiInfo(new ApiInfo("Code Hospitality ADMIN API definitions", "Code Hospitality ADMIN API definitions", "1.0", null, (Contact) null, null, null, Collections.emptyList()));
	}

	private SecurityContext generateXAuthTokenSecurityContext() {
		return SecurityContext.builder()
		  .forPaths(PathSelectors.any())
		  .securityReferences(Collections.singletonList(new SecurityReference("X-Auth-Token", new AuthorizationScope[]{new AuthorizationScope("global", "Global security description")})))
		  .build();
	}
}