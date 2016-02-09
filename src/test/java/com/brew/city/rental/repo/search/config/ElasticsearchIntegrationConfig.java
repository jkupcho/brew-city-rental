package com.brew.city.rental.repo.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.brew.city.rental.BrewCityRentalApplication;

@Import(BrewCityRentalApplication.class)
@Configuration
@Profile("testElasticSearch")
public class ElasticsearchIntegrationConfig {

	@Bean
	public IndexBuilder indexBuilder() {
		return new IndexBuilder();
	}
	
}
