package com.brew.city.rental.repo.search;

import static org.elasticsearch.index.query.QueryBuilders.fuzzyLikeThisQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.avg;
import static org.elasticsearch.search.aggregations.AggregationBuilders.nested;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.brew.city.rental.domain.Movie;
import com.brew.city.rental.repo.search.config.ElasticsearchIntegrationConfig;
import com.brew.city.rental.repo.search.config.IndexBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ElasticsearchIntegrationConfig.class)
@ActiveProfiles({"local", "testElasticSearch"})
public class MovieSearchRepositoryIntegrationTest {

	@Autowired
	private IndexBuilder indexBuilder;

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Before
	public void setup() throws InterruptedException {
		indexBuilder.indexMovies();
	}
	
	@Test
	public void aggregationTest() {
		SearchQuery ratingsAggregation = new NativeSearchQueryBuilder()
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
		
		Aggregations aggregations = elasticsearchTemplate.query(ratingsAggregation, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});
		
		Aggregation aggregation = aggregations.get("group_by_movie_title");
		assertNotNull(aggregation);
	}
	
	@Test
	public void fuzzyTest() {
		SearchQuery fuzzyTitleQuery = new NativeSearchQueryBuilder()
				.withIndices("movies")
				.withQuery(
					fuzzyLikeThisQuery("title")
						.likeText("Andre's Mo")
						.fuzziness(Fuzziness.fromSimilarity(0.3f))
				).build();
		
		List<Movie> movies = elasticsearchTemplate.queryForList(fuzzyTitleQuery, Movie.class);
		assertEquals(1, movies.size());
	}

}
