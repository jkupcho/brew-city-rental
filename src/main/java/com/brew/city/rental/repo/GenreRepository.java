package com.brew.city.rental.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.brew.city.rental.domain.Genre;

public interface GenreRepository extends PagingAndSortingRepository<Genre, Long> {

}
