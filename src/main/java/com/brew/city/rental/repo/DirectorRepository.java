package com.brew.city.rental.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.brew.city.rental.domain.Director;

@RepositoryRestResource
public interface DirectorRepository extends PagingAndSortingRepository<Director, Long> {

}
