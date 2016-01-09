package com.brew.city.rental.repo.search;

import static org.elasticsearch.index.query.QueryBuilders.wrapperQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.avg;
import static org.elasticsearch.search.aggregations.AggregationBuilders.nested;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.brew.city.rental.domain.Movie;
import com.brew.city.rental.repo.DirectorRepository;
import com.brew.city.rental.repo.MovieRepository;
import com.brew.city.rental.repo.ReviewRepository;

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
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
			.withIndices("movies")
			.addAggregation(
				terms("group_by_movie_title")
					.field("title")
				.subAggregation(
					nested("reviews").path("reviews")
					.subAggregation(
						avg("average_rating")
							.field("rating")
					)
				)
			)
			.build();
		
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});
		
		Aggregation aggregation = aggregations.get("group_by_movie_title");
		assertNotNull(aggregation);
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

}
