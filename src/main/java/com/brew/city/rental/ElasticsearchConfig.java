package com.brew.city.rental;

import org.elasticsearch.node.NodeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactoryBean;

import com.brew.city.rental.ElasticsearchConfig.RestElasticSearchFactoryBean;

@Configuration
public class ElasticsearchConfig {

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		return new ElasticsearchTemplate(NodeBuilder.nodeBuilder().data(true).local(true).node().client());
	}

	@Bean
	@Profile("local")
	public IndexBuilder indexBuilder() {
		return new IndexBuilder();
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static class RestElasticSearchFactoryBean extends ElasticsearchRepositoryFactoryBean {

		@Override
		public void afterPropertiesSet() {
			setMappingContext(new SimpleElasticsearchMappingContext());
			super.afterPropertiesSet();
		}
		
	}
}
