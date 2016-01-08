package com.brew.city.rental.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/***
 * <p>{@link MovieCopy} signifies an on-hand copy of a movie title.</p>
 * 
 * @author Jonathan Kupcho
 *
 */
@Entity
public class MovieCopy {
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="movie_id")
	private Movie movie;
	
	private boolean available;
	
	/***
	 * Id is auto-generated at time of entry.
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Each {@link MovieCopy} is the owning relation
	 * on a {@link Movie}
	 */
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	/**
	 * Signifies the movie has been checked out by a customer.
	 */
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
}
