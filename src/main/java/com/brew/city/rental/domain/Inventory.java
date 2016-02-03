package com.brew.city.rental.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/***
 * <p>{@link Inventory} signifies an on-hand copy of a movie title.</p>
 * 
 * @author Jonathan Kupcho
 *
 */
@Entity
public class Inventory {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="movie_id")
	private Movie movie;
	
	private Integer stock;
	
	private Integer onHand;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Movie getMovie() {
		return movie;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	public Integer getStock() {
		return stock;
	}
	
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
	public Integer getOnHand() {
		return onHand;
	}
	
	public void setOnHand(Integer onHand) {
		this.onHand = onHand;
	}
	
}
