package com.brew.city.rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.brew.city.rental.ElasticsearchConfig.RestElasticSearchFactoryBean;

@SpringBootApplication
@EnableJpaRepositories("com.brew.city.rental.repo")
@EnableElasticsearchRepositories(basePackages="com.brew.city.rental.repo.search", repositoryFactoryBeanClass=RestElasticSearchFactoryBean.class)
public class BrewCityRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrewCityRentalApplication.class, args);
	}
}
