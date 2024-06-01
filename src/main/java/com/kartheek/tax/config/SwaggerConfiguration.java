package com.kartheek.tax.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SwaggerConfiguration {

	@Value("${build.version}")
	private String buildVersion;
	@Value("${application.name}")
	private String applicationName;

	public static final String TAX_CALCULATION_API = "Tax Calculation API";

	@Bean
	public OpenAPI customOpenAPI() {
		final Info info = new Info().title(applicationName).description(applicationName + " reference manual")
				.version(buildVersion);

		return new OpenAPI().components(new Components()).addTagsItem(createTag(TAX_CALCULATION_API, ""))
				// Other tags here...
				.info(info);
	}

	private Tag createTag(String name, String description) {
		final Tag tag = new Tag();
		tag.setName(name);
		tag.setDescription(description);
		return tag;
	}

}
