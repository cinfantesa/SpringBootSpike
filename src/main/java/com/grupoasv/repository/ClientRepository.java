package com.grupoasv.repository;

import com.grupoasv.domain.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientRepository extends CrudRepository<Client,Integer>{
}
