package com.brew.city.rental.repo.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.wrapperQuery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.brew.city.rental.BrewCityRentalApplication;
import com.brew.city.rental.IndexBuilder;
import com.brew.city.rental.domain.Director;
import com.brew.city.rental.domain.Movie;
import com.brew.city.rental.domain.Review;
import com.brew.city.rental.repo.DirectorRepository;
import com.brew.city.rental.repo.MovieRepository;
import com.brew.city.rental.repo.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BrewCityRentalApplication.class)
@ActiveProfiles("local")
public class MovieSearchRepositoryIntegrationTest {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private DirectorRepository directorRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private MovieSearchRepository movieSearchRepository;

	@Autowired
	private ReviewSearchRepository reviewSearchRepository;
	
	@Autowired
	private IndexBuilder indexBuilder;

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Before
	public void setup() throws InterruptedException {
		indexBuilder.indexMovies();
	}
	
	@Test
	public void fields() throws Exception {
		ClassPathResource rating = new ClassPathResource("json/reviews/average_rating.json");
		String queryString = IOUtils.toString(rating.getInputStream());
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
			.withIndices("reviews")
			.withQuery(wrapperQuery(queryString))
			.build();
		
		elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Object>() {

			@Override
			public Object extract(SearchResponse response) {
				return null;
			}
			
		});
	}
	
	@Transactional
	@Rollback(true)
	@Test
	public void testMovieQueries() throws Exception {
		ClassPathResource allTerm = new ClassPathResource("json/movies/all_term.json");
		ClassPathResource nolan = new ClassPathResource("json/movies/nolan.json");
		
		String query = FileUtils.readFileToString(allTerm.getFile());
		
		Page<Movie> page = movieSearchRepository.search(wrapperQuery(query), new PageRequest(0, 10));
		
		assertEquals(1, page.getNumberOfElements());
		
		query = FileUtils.readFileToString(nolan.getFile());
		
		int resultSize = StreamSupport
				.stream(movieSearchRepository.search(wrapperQuery(query)).spliterator(), false)
				.collect(Collectors.toList()).size();
		
		assertEquals(1, resultSize);
	}

	@Transactional
	@Rollback(true)
	@Test
	public void findElasticSearch() throws Exception {
		Review review = reviewRepository.findOne(1L);
		
		assertNotNull(review.getMovie());
		
		assertEquals(1, reviewRepository.count());
		assertEquals(1, movieRepository.count());
		
		List<Movie> searchResult = StreamSupport
				.stream(movieSearchRepository.search(queryStringQuery("Nolan")).spliterator(), false)
				.collect(Collectors.toList());

		assertEquals(1, searchResult.size());
		Movie movie = searchResult.get(0);
		assertEquals("Andre's Movie", movie.getTitle());

		Director director = directorRepository.findOne(2L);
		movie.setDirector(director);

		movieRepository.save(movie);
		movieSearchRepository.save(movie);
		
		ClassPathResource resource = new ClassPathResource("json/nolan.json");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String query = objectMapper.writeValueAsString(FileUtils.readFileToString(resource.getFile()));
		
		searchResult = StreamSupport
				.stream(movieSearchRepository.search(wrapperQuery(query)).spliterator(), false)
				.collect(Collectors.toList());

		assertEquals(1, searchResult.size());
		movie = searchResult.get(0);
		assertEquals("Andre's Movie", movie.getTitle());
	}

}
