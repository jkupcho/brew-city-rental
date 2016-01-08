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
import javax.persistence.ManyToOne;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Document(indexName = "movie")
public class Movie {
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private Long id;

	private String title;
	
	@ManyToOne
	@JoinColumn(name="genre_id")
	private Genre genre;
	
	private Integer released;
	
	private Integer length;
	
	@ManyToOne
	@JoinColumn(name="language_id")
	private Language language;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name="movie_actor_xref",
			   joinColumns = { @JoinColumn(name="movie_id", referencedColumnName="id") },
			   inverseJoinColumns = { @JoinColumn(name="actor_id", referencedColumnName="id") })
	private Set<Actor> actors = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name="director_id")
	private Director director;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public Integer getReleased() {
		return released;
	}

	public void setReleased(Integer released) {
		this.released = released;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Set<Actor> getActors() {
		return actors;
	}

	public void setActors(Set<Actor> actors) {
		this.actors = actors;
	}

	public Director getDirector() {
		return director;
	}

	public void setDirector(Director director) {
		this.director = director;
	}
	
}
