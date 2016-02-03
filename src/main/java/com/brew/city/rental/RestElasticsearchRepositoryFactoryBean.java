package com.brew.city.rental;

import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactoryBean;

public class RestElasticsearchRepositoryFactoryBean extends ElasticsearchRepositoryFactoryBean  {
	
	@Override
	public void afterPropertiesSet() {
		setMappingContext(new SimpleElasticsearchMappingContext());
		super.afterPropertiesSet();
	}
	
}
