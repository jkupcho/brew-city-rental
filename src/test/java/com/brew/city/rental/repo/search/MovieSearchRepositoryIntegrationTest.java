package com.brew.city.rental.repo.search;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.brew.city.rental.BrewCityRentalApplication;
import com.brew.city.rental.domain.Movie;
import com.brew.city.rental.repo.MovieRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BrewCityRentalApplication.class)
public class MovieSearchRepositoryIntegrationTest {
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private MovieSearchRepository movieSearchRepository;
	
	@Before
	public void setup() {
		Movie movie  = movieRepository.findOne(1L);
		movieSearchRepository.save(movie);
	}
	
	@Test
	public void findElasticSearch() {
		List<Movie> searchResult = StreamSupport
	        .stream(movieSearchRepository.search(queryStringQuery("Nolan")).spliterator(), false)
	        .collect(Collectors.toList());
		
		assertEquals(1, searchResult.size());
		Movie movie = searchResult.get(0);
		
		assertEquals("Andre Movie", movie.getTitle());
	}
	
}
