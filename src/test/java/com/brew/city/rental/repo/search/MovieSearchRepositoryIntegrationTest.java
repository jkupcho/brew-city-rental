package com.brew.city.rental.repo.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.brew.city.rental.BrewCityRentalApplication;
import com.brew.city.rental.IndexBuilder;
import com.brew.city.rental.domain.Director;
import com.brew.city.rental.domain.Movie;
import com.brew.city.rental.repo.DirectorRepository;
import com.brew.city.rental.repo.MovieRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BrewCityRentalApplication.class)
@ActiveProfiles("local")
public class MovieSearchRepositoryIntegrationTest {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private DirectorRepository directorRepository;

	@Autowired
	private MovieSearchRepository movieSearchRepository;
	
	@Autowired
	private IndexBuilder indexBuilder;

	@Before
	public void setup() {
		indexBuilder.indexMovies();
	}

	@Transactional
	@Rollback(true)
	@Test
	public void findElasticSearch() {
		List<Movie> searchResult = StreamSupport
				.stream(movieSearchRepository.search(queryStringQuery("Nolan")).spliterator(), false)
				.collect(Collectors.toList());

		assertEquals(1, searchResult.size());
		Movie movie = searchResult.get(0);
		assertEquals("Andre Movie", movie.getTitle());

		Director director = directorRepository.findOne(2L);
		movie.setDirector(director);

		movieRepository.save(movie);
		movieSearchRepository.save(movie);

		searchResult = StreamSupport
				.stream(movieSearchRepository.search(queryStringQuery("Blow")).spliterator(), false)
				.collect(Collectors.toList());

		assertEquals(1, searchResult.size());
		movie = searchResult.get(0);
		assertEquals("Andre Movie", movie.getTitle());
	}

}
