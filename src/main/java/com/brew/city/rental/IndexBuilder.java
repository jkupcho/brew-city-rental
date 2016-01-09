package com.brew.city.rental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.brew.city.rental.repo.MovieRepository;
import com.brew.city.rental.repo.ReviewRepository;
import com.brew.city.rental.repo.search.MovieSearchRepository;
import com.brew.city.rental.repo.search.ReviewSearchRepository;

public class IndexBuilder {

	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private MovieSearchRepository movieSearchRepository;
	
	@Autowired
	private ReviewSearchRepository reviewSearchRepository;

	private static final Logger logger = LoggerFactory.getLogger(IndexBuilder.class);
	
	@Transactional
	public void indexMovies() {
		logger.debug("Loading movies into elasticsearch index");
		movieRepository.findAll().forEach(movieSearchRepository::save);
		reviewRepository.findAll().forEach(reviewSearchRepository::save);
		logger.debug("Finished indexing movies");
	}

}
