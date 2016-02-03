package com.brew.city.rental.repo.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.brew.city.rental.domain.Movie;

public interface MovieSearchRepository extends ElasticsearchRepository<Movie, Long> {

}
