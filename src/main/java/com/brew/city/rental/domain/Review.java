package com.brew.city.rental.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Entity
@Document(indexName = "reviews")
public class Review {
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private Long id;

	@ManyToOne
	@JoinColumn(name="user_id")
	@Field(type=FieldType.Object)
	private User user;
	
	@ManyToOne
	@JoinColumn(name="movie_id")
	private Movie movie;
	
	private Integer rating;
	
	private String text;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
