package com.brew.city.rental.repo.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.brew.city.rental.domain.Review;

public interface ReviewSearchRepository extends ElasticsearchRepository<Review, Long> {

}
