package com.brew.city.rental.repo.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.brew.city.rental.domain.Review;

@RepositoryRestResource(path="/_search/review")
public interface ReviewSearchRepository extends ElasticsearchRepository<Review, Long> {

}
