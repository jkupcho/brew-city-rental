package com.brew.city.rental.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class User extends Person {

	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private Long id;

	@ManyToMany
	@JoinTable(
		name="user_review_xref",
		joinColumns={ @JoinColumn(name="user_id", referencedColumnName="id") },
		inverseJoinColumns={ @JoinColumn(name="movie_id", referencedColumnName="id") }
	)
	private Set<Review> reviews = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}
	
}
