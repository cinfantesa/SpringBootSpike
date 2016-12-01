package com.grupoasv.repository;

import com.grupoasv.domain.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ServiceRepository extends CrudRepository<Service, Long>{}
