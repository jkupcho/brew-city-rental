package com.brew.city.rental.repo.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.brew.city.rental.domain.Movie;

@RepositoryRestResource(path="/_search/movies")
public interface MovieSearchRepository extends ElasticsearchRepository<Movie, Long> {

}
